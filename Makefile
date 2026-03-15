TODAY   = $(shell date +%Y-%m-%d)
VERSION = Fusion-$(TODAY)
JAR     = target/rotp-$(VERSION).jar
CPFILE  = target/classpath.txt

.PHONY: build check compile dev run clean

# macOS build — update pom version to today's date, then build full jar
build:
	sed -i '' 's|<version>Fusion-[0-9]*-[0-9]*-[0-9]*</version>|<version>$(VERSION)</version>|' pom.xml
	mvn package -DskipTests -Pmac; \
	test -f $(JAR) && echo "BUILD OK: $(JAR)"

# compile-only check — no packaging, no jar overwrite
check:
	mvn compile -q && echo "COMPILE OK"

compile:
	mvn compile -q

# fast dev cycle — compile only changed files, run from classes (no jar rebuild)
# filter out webp-imageio (x86_64-only native lib, not needed for full assets)
dev: $(CPFILE)
	mvn compile -q && \
	java -cp "target/classes:$$(grep -o '[^:]*' $(CPFILE) | grep -v webp-imageio | paste -sd: -)" rotp.RotpGovernor

$(CPFILE):
	mvn dependency:build-classpath -q -Dmdep.outputFile=$(CPFILE)

run: $(JAR)
	java -jar $(JAR)

clean:
	mvn clean -q
