package com.lgcns.test.sourceanalyzer;

public class SourceAnalyzerRun {

    // data/sourceanalyzer/dir1 또는 data/sourceanalyzer/dir2 로 테스트를 실시한다.
    public static void main(String[] args) {
        /**
         * [분석 결과] 
         * 전체 소스 개수 : 5 
         * 문제 소스 개수 : 1 
         * 소스 전체 Line 수 : 5221 
         * 검색된 패턴수 : 2
         */
        printResult("data/sourceanalyzer/dir1", "public void remove() {removeDescending();}");

        /**
         * [분석 결과]
         * 전체 소스 개수 : 1
         * 문제 소스 개수 : 0
         * 소스 전체 Line 수 : 73
         * 검색된 패턴수 : 0
         */
//         printResult("data/sourceanalyzer/dir2", "public void remove() {removeDescending();}");
        
        /**
         * [분석 결과]
         * 전체 소스 개수 : 5
         * 문제 소스 개수 : 1
         * 소스 전체 Line 수 : 5221
         * 검색된 패턴수 : 1
         */
//         printResult("data/sourceanalyzer/dir1", "     * @return the number of elements in this collection     */    int size();");
        
        /**
         * [분석 결과]
         * 전체 소스 개수 : 1
         * 문제 소스 개수 : 1
         * 소스 전체 Line 수 : 73
         * 검색된 패턴수 : 2
         */
         printResult("data/sourceanalyzer/dir2", "     * @return the number of elements in this collection     */    int size();");
    }

    public static void printResult(String dirName, String pattern) {
        System.out.println("=====================================================");
        System.out.println("대상 디렉토리 : " + dirName);
        System.out.println("검색 패턴 : " + pattern + "\n");

        SourceAnalyzer analyzer = new SourceAnalyzer();

        analyzer.analyze(dirName, pattern);
        System.out.println("[분석 결과]");
        System.out.println("전체 소스 개수 : " + analyzer.getJavaFileCount());
        System.out.println("문제 소스 개수 : " + analyzer.getProblemFileCount());
        System.out.println("소스 전체 Line 수 : " + analyzer.getTotalLineCount());
        System.out.println("검색된 패턴수 : " + analyzer.getPatternCount());
        System.out.println("=====================================================");
    }
}
