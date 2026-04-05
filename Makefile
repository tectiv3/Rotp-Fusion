TODAY   = $(shell date +%Y-%m-%d)
VERSION = Fusion-$(TODAY)
JAR     = target/rotp-$(VERSION).jar
CPFILE  = target/classpath.txt
MVN     = unset JAVA_HOME && nix shell nixpkgs\#maven -c mvn -Dmaven.repo.local=.m2/repository -Pmac

.PHONY: build check compile dev run clean

# macOS build — update pom version to today's date, then build full jar
build:
	sed -i '' 's|<version>Fusion-[0-9]*-[0-9]*-[0-9]*</version>|<version>$(VERSION)</version>|' pom.xml
	$(MVN) package -DskipTests -Pmac; \
	test -f $(JAR) && echo "BUILD OK: $(JAR)"

# compile-only check — no packaging, no jar overwrite
check:
	$(MVN) compile -q && echo "COMPILE OK"

compile:
	$(MVN) compile -q

# fast dev cycle — compile only changed files, run from classes (no jar rebuild)
# filter out webp-imageio (x86_64-only native lib, not needed for full assets)
dev: $(CPFILE)
	$(MVN) compile -q && \
	java -cp "target/classes:$$(grep -o '[^:]*' $(CPFILE) | grep -v webp-imageio | paste -sd: -)" rotp.RotpGovernor

$(CPFILE):
	$(MVN) dependency:build-classpath -q -Dmdep.outputFile=$(CPFILE)

run: $(JAR)
	java -jar $(JAR)

clean:
	$(MVN) clean -q
