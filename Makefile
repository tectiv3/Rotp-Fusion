TODAY   = $(shell date +%Y-%m-%d)
VERSION = Fusion-$(TODAY)
JAR     = target/rotp-$(VERSION).jar
MINI    = target/rotp-$(VERSION)-mini.jar

.PHONY: build compile run run-mini clean

# macOS build — update pom version to today's date, then build
build:
	sed -i '' 's|<version>Fusion-[0-9]*-[0-9]*-[0-9]*</version>|<version>$(VERSION)</version>|' pom.xml
	mvn package -DskipTests -Dmaven.assembly.skipAssembly=true; \
	test -f $(JAR) && echo "BUILD OK: $(JAR)"

compile:
	mvn compile -q

run: $(JAR)
	java -jar $(JAR)

run-mini: $(MINI)
	java -jar $(MINI)

clean:
	mvn clean -q
