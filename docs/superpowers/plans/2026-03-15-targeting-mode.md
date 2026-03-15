# Targeting Mode Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add context-aware targeting mode to galaxy map so `spriteAt()` only returns valid target types during targeting actions (transport, abandon, rally, fleet deploy).

**Architecture:** A `TargetMode` enum on `GalaxyMapPanel` gates sprite iteration in `spriteAt()`. Entry points set the mode, `MainUI.clickedSprite()` auto-clears it. Crosshair cursor signals targeting visually. Dwell delay bypassed during targeting.

**Tech Stack:** Java 17, Swing

**Spec:** `docs/superpowers/specs/2026-03-15-targeting-mode-design.md`

---

## File Structure

| File | Role |
|------|------|
| `src/rotp/ui/main/GalaxyMapPanel.java` | Add `TargetMode` enum, field, setter/clearer, filter in `spriteAt()`, dwell bypass |
| `src/rotp/ui/main/MainUI.java` | Auto-clear targeting in `clickedSprite()` setter |
| `src/rotp/ui/main/EmpireSystemPanel.java` | Set targeting on transport/abandon/rally clicks |
| `src/rotp/ui/main/FleetPanel.java` | Set targeting when player fleet selected for deployment |

---

## Task 1: Add TargetMode enum and state to GalaxyMapPanel

**Files:**
- Modify: `src/rotp/ui/main/GalaxyMapPanel.java:146-155` (fields), `:208-209` (constructor area)

- [ ] **Step 1: Add the enum and field**

After the existing `private boolean searchingSprite = false;` field (line 147), add:

```java
public enum TargetMode { NONE, SYSTEM }
private TargetMode targetMode = TargetMode.NONE;
```

- [ ] **Step 2: Add setTargetMode and clearTargetMode methods**

After the existing `cancelPendingHover()` method (line 208), add:

```java
public void setTargetMode(TargetMode mode) {
    targetMode = mode;
    if (mode != TargetMode.NONE)
        setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR));
}
public void clearTargetMode() {
    targetMode = TargetMode.NONE;
    setCursor(java.awt.Cursor.getDefaultCursor());
}
public TargetMode targetMode() { return targetMode; }
```

- [ ] **Step 3: Compile check**

Run: `make check`
Expected: COMPILE OK

- [ ] **Step 4: Commit**

```
git add src/rotp/ui/main/GalaxyMapPanel.java
git commit -m "Add TargetMode enum and state to GalaxyMapPanel"
```

---

## Task 2: Filter spriteAt() when targeting

**Files:**
- Modify: `src/rotp/ui/main/GalaxyMapPanel.java:1157-1263` (`spriteAt` method)

- [ ] **Step 1: Add systemsOnly flag at top of spriteAt()**

After the overlay mask check (line 1177 `return null;`), before `Galaxy gal = galaxy();`, add:

```java
boolean systemsOnly = (targetMode == TargetMode.SYSTEM);
```

- [ ] **Step 2: When systemsOnly, skip to system iteration directly**

Replace the section from `// if ctrl down` through the end of the method (lines 1182-1262) with logic that checks `systemsOnly`:

```java
// In targeting mode, only match star systems
if (systemsOnly) {
    if (parent.hoverOverSystems()) {
        for (int id=0;id<gal.numStarSystems();id++) {
            if (gal.system(id).isSelectableAt(this, x1, y1))
                return gal.system(id);
        }
    }
    return null;
}
```

Place this block right after `boolean systemsOnly = ...;` and before the existing ctrl-down check. The existing code below remains unchanged (it only runs when `systemsOnly` is false, i.e., normal mode).

- [ ] **Step 3: Compile check**

Run: `make check`
Expected: COMPILE OK

- [ ] **Step 4: Commit**

```
git add src/rotp/ui/main/GalaxyMapPanel.java
git commit -m "Filter spriteAt() to only return star systems in targeting mode"
```

---

## Task 3: Update dwell bypass to use targetMode

**Files:**
- Modify: `src/rotp/ui/main/GalaxyMapPanel.java:~1404` (mouseMoved dwell section)

- [ ] **Step 1: Replace the dwell bypass condition**

Current code:
```java
if (UserPreferences.sensitivityDwell() && parent.clickedSprite() == null) {
```

Replace with:
```java
if (UserPreferences.sensitivityDwell() && targetMode == TargetMode.NONE) {
```

This is cleaner — targeting mode is the explicit signal, not an indirect check on clickedSprite.

- [ ] **Step 2: Compile check**

Run: `make check`
Expected: COMPILE OK

- [ ] **Step 3: Commit**

```
git add src/rotp/ui/main/GalaxyMapPanel.java
git commit -m "Bypass dwell delay based on targeting mode instead of clickedSprite"
```

---

## Task 4: Auto-clear targeting in MainUI.clickedSprite()

**Files:**
- Modify: `src/rotp/ui/main/MainUI.java:693-698` (`clickedSprite` setter)

- [ ] **Step 1: Add import for ShipRelocationSprite**

Add to imports section:
```java
import rotp.ui.sprites.ShipRelocationSprite;
```

- [ ] **Step 2: Add targeting auto-clear logic**

Replace the current `clickedSprite` setter:

```java
public void clickedSprite(Sprite s)      {
    map.cancelPendingHover();
    sessionVar("MAINUI_CLICKED_SPRITE", s);
    if (s instanceof StarSystem)
        lastSystemSelected(s);
}
```

With:

```java
public void clickedSprite(Sprite s)      {
    map.cancelPendingHover();
    // Auto-clear targeting unless the new sprite is an active targeting action
    boolean keepTargeting = (s instanceof SystemTransportSprite)
        || (s instanceof ShipRelocationSprite)
        || (s instanceof ShipFleet && ((ShipFleet) s).canBeSentBy(player()));
    if (!keepTargeting)
        map.clearTargetMode();
    sessionVar("MAINUI_CLICKED_SPRITE", s);
    if (s instanceof StarSystem)
        lastSystemSelected(s);
}
```

- [ ] **Step 3: Compile check**

Run: `make check`
Expected: COMPILE OK

- [ ] **Step 4: Commit**

```
git add src/rotp/ui/main/MainUI.java
git commit -m "Auto-clear targeting mode when clickedSprite changes to non-targeting type"
```

---

## Task 5: Set targeting mode from EmpireSystemPanel buttons

**Files:**
- Modify: `src/rotp/ui/main/EmpireSystemPanel.java:907-944` (click handlers)

- [ ] **Step 1: Add targeting mode activation to rally point click**

At line 911, after `parentSpritePanel.parent.clickedSprite(sys.rallySprite());`, add:
```java
parentSpritePanel.parent.map().setTargetMode(GalaxyMapPanel.TargetMode.SYSTEM);
```

- [ ] **Step 2: Add targeting mode activation to transport click**

At line 929, after `parentSpritePanel.parent.clickedSprite(sys.transportSprite());`, add:
```java
parentSpritePanel.parent.map().setTargetMode(GalaxyMapPanel.TargetMode.SYSTEM);
```

- [ ] **Step 3: Add targeting mode activation to abandon click**

At line 940, after `parentSpritePanel.parent.clickedSprite(sys.transportSprite());`, add:
```java
parentSpritePanel.parent.map().setTargetMode(GalaxyMapPanel.TargetMode.SYSTEM);
```

- [ ] **Step 4: Add import**

Add to imports:
```java
import rotp.ui.main.GalaxyMapPanel;
```

- [ ] **Step 5: Compile check**

Run: `make check`
Expected: COMPILE OK

- [ ] **Step 6: Commit**

```
git add src/rotp/ui/main/EmpireSystemPanel.java
git commit -m "Set targeting mode on transport, abandon, and rally button clicks"
```

---

## Task 6: Set targeting mode from FleetPanel

**Files:**
- Modify: `src/rotp/ui/main/FleetPanel.java:339-353` (`useClickedSprite` method)

- [ ] **Step 1: Activate targeting when player fleet accepted for deployment**

In `useClickedSprite()`, inside the `if (o instanceof ShipFleet)` block (line 347), after `selectNewFleet(clickedFleet);` (line 352), add targeting activation:

```java
if (o instanceof ShipFleet) {
    ShipFleet clickedFleet = (ShipFleet) o;
    if (clickedFleet.empire() != player())
        return false;
    if (clickedFleet != selectedFleet())
        selectNewFleet(clickedFleet);
    if (clickedFleet.canBeSentBy(player()))
        parent.parent.map().setTargetMode(GalaxyMapPanel.TargetMode.SYSTEM);
    return false;
}
```

- [ ] **Step 2: Clear targeting on fleet cancel**

In `cancelFleet()` method (line 245), add clear before the existing code:

```java
public void cancelFleet() {
    parent.parent.map().clearTargetMode();
    selectNewFleet(null);
    parent.parent.reselectCurrentSystem();
}
```

- [ ] **Step 3: Clear targeting on sendFleet success**

In `sendFleet()` method (line 208), the method already calls `cancelFleet()` in most paths, which now clears targeting. But for the path at line 228 where a partial fleet is sent and the panel stays open with the remaining fleet, targeting should stay active — which it does since `selectNewFleet` is called (and `clickedSprite` stays as a fleet). No change needed.

- [ ] **Step 4: Add import**

Add to imports:
```java
import rotp.ui.main.GalaxyMapPanel;
```

- [ ] **Step 5: Compile check**

Run: `make check`
Expected: COMPILE OK

- [ ] **Step 6: Commit**

```
git add src/rotp/ui/main/FleetPanel.java
git commit -m "Set targeting mode when player fleet selected for deployment"
```

---

## Task 7: Final integration verification

- [ ] **Step 1: Full compile**

Run: `make check`
Expected: COMPILE OK

- [ ] **Step 2: Build for testing**

Run: `make build`

- [ ] **Step 3: Manual test checklist**

Test each targeting scenario:
1. Select a planet → click Send Transports → cursor should be crosshair → hover should only highlight systems, not nearby fleets → click target → transports sent, cursor returns to normal
2. Select a planet → click Abandon → same behavior as transports
3. Select a planet → click Set Rally Point → same crosshair + system-only targeting
4. Click a player fleet → cursor should be crosshair → hover only systems → click destination → fleet deployed, cursor normal
5. Click an enemy fleet → cursor should NOT be crosshair (inspection only)
6. While in any targeting mode, press ESC → targeting clears, cursor normal
7. Test with dwell sensitivity: all targeting actions should have immediate hover response
8. Test with click-only sensitivity: verify no regression
