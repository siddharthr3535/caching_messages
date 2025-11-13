public class Main {
    public static void main(String[] args) {
       Test test = new Test();
       test.runTests();
       CacheTest cacheTest = new CacheTest();
       cacheTest.runTests();
       CacheEvaluation cacheEvaluation = new CacheEvaluation();
         cacheEvaluation.runTests();
    }
}