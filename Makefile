JC = javac
JAVA = java


JFLAGS = -g

MSG_DIR = messages


SOURCES = Cache.java \
          CacheStorage.java \
          CacheEvaluation.java \
          CacheTest.java \
          Config.java \
          Main.java \
          Message.java \
          MessageStorage.java \
          Strategy.java \
          Test.java


CLASSES = $(SOURCES:.java=.class)


MAIN_CLASS = Main
TEST_CLASS = Test
CACHE_TEST_CLASS = CacheTest
EVAL_CLASS = CacheEvaluation


all: compile


compile: $(SOURCES)
	$(JC) $(JFLAGS) $(SOURCES)
	@echo "Compilation complete!"


run: compile
	$(JAVA) $(MAIN_CLASS)


test: compile
	$(JAVA) $(TEST_CLASS)


cache-test: compile
	$(JAVA) $(CACHE_TEST_CLASS)


eval: compile
	$(JAVA) $(EVAL_CLASS)


test-all: test cache-test eval


clean:
	rm -f *.class
	rm -rf $(MSG_DIR)
	@echo "Cleaned build artifacts and message files"


clean-messages:
	rm -rf $(MSG_DIR)
	@echo "Cleaned message files"


rebuild: clean all