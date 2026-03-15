# Targeting Mode for Galaxy Map Sprite Selection

## Problem

The galaxy map's `spriteAt()` method returns the highest-priority sprite at the cursor regardless of context. When the player is targeting a star system (sending transports, deploying a fleet, setting a rally point), fleet icons near the target system hijack hover detection. This makes targeting unreliable — especially in dwell sensitivity mode where delayed hover compounds the problem.

## Solution

Add a **targeting mode** to `GalaxyMapPanel` that filters `spriteAt()` to only return sprites matching the current action's target type. When targeting mode is active: hover detection is immediate (bypasses dwell delay), the cursor changes to a crosshair, and only valid target types are selectable.

## Design

### Targeting State

A `TargetMode` enum on `GalaxyMapPanel`:

```java
public enum TargetMode { NONE, SYSTEM }
```

All four targeting actions (transport, abandon, rally, fleet deploy) target star systems, so a single `SYSTEM` mode covers everything. The enum is extensible for future target types.

Fields and methods on `GalaxyMapPanel`:

- `private TargetMode targetMode = TargetMode.NONE`
- `setTargetMode(TargetMode mode)` — sets mode + crosshair cursor
- `clearTargetMode()` — resets to NONE + default cursor

### `spriteAt()` Filter

When `targetMode == SYSTEM`:

- Skip fleet/ship iteration entirely (not "iterate then reject" — zero wasted work)
- Skip flight path checks
- Skip transport sprite checks
- Only run the star system iteration
- Ignore Ctrl-key priority swap (systems always win)

This is an early-exit optimization at the top of each sprite search section.

### Lifecycle

**Setting targeting mode** — each action entry point calls `map.setTargetMode(SYSTEM)`:

| Action | Entry Point | File |
|--------|-------------|------|
| Send Transports | Transport button click | `EmpireSystemPanel.java` |
| Abandon Colony | Abandon button click | `EmpireSystemPanel.java` |
| Rally Point | Rally button click | `EmpireSystemPanel.java` |
| Fleet Deployment | Fleet becomes clicked sprite | `FleetPanel.java` |

**Clearing targeting mode** — automatic cleanup in `MainUI.clickedSprite(Sprite s)`:

When the new clicked sprite is NOT a targeting sprite (not `SystemTransportSprite`, `ShipRelocationSprite`, or `ShipFleet`), call `clearTargetMode()`. This provides a single cleanup point rather than scattering clear calls.

Also cleared explicitly on: ESC key, Cancel button, right-click cancel.

### Cursor

- `setTargetMode(SYSTEM)` → `setCursor(Cursor.CROSSHAIR_CURSOR)`
- `clearTargetMode()` → `setCursor(Cursor.DEFAULT_CURSOR)`

Hover highlight: existing system hover ring is sufficient. Invalid targets (out of range) hover normally but the panel shows the "out of range" message as it already does.

### Dwell Sensitivity Integration

Replace the current `clickedSprite() != null` dwell bypass with:

```java
if (UserPreferences.sensitivityDwell() && targetMode == TargetMode.NONE) {
    // dwell delay logic — only active when NOT targeting
}
```

Targeting mode always gets immediate hover detection regardless of sensitivity setting.

## Files Changed

| File | Change |
|------|--------|
| `GalaxyMapPanel.java` | Add `TargetMode` enum, `targetMode` field, `setTargetMode()`/`clearTargetMode()`, filter in `spriteAt()`, dwell bypass update |
| `MainUI.java` | In `clickedSprite(Sprite s)`, auto-clear targeting when sprite isn't a targeting type |
| `EmpireSystemPanel.java` | Set targeting mode on transport/abandon/rally button clicks |
| `FleetPanel.java` | Set targeting mode when fleet becomes clicked sprite |

## Constraints

- Performance: `spriteAt()` is called on every mouse move. The filter adds one enum comparison at the top, then skips entire iteration blocks. Net performance improvement during targeting.
- No new classes or interfaces. The enum can be a nested type inside `GalaxyMapPanel`.
- Backward compatible: `TargetMode.NONE` is the default, so all existing behavior is unchanged when not targeting.
