package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelReportUtil {

    public static List<TestResult> results = new ArrayList<>();
    public static Set<String> executedFeatures = new HashSet<>();

    private static final String REPORT_PATH = "reports/Automation_Report.xlsx";
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";
    private static final String PROPERTIES_FILE = "src/main/resources/application.properties";

    public static class TestResult {
        public String testCaseId;
        public String testCaseName;
        public double duration;
        public String executedBy;
        public String status;
        public String errorMessage;
        public String screenshotPath;
        public String featureName;

        public TestResult(String testCaseId, String testCaseName, double duration, String executedBy,
                          String status, String errorMessage, String screenshotPath, String featureName) {
            this.testCaseId = testCaseId;
            this.testCaseName = testCaseName;
            this.duration = duration;
            this.executedBy = executedBy;
            this.status = status;
            this.errorMessage = errorMessage;
            this.screenshotPath = screenshotPath;
            this.featureName = featureName;
        }
    }

    public static String captureScreenshot(WebDriver driver, String testCaseId) {
        try {
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotFileName = SCREENSHOT_DIR + testCaseId + "_" + timestamp + ".png";
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
            Files.write(Paths.get(screenshotFileName), screenshotBytes);
            return screenshotFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void generateExcelReport() {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File file = new File(REPORT_PATH);
            Workbook workbook;
            Sheet sheet;

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheet(date);
                if (sheet == null) {
                    sheet = workbook.createSheet(date);
                    addSummaryBlock(sheet, workbook);
                    addHeaderRow(sheet, workbook);
                }
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet(date);
                addSummaryBlock(sheet, workbook);
                addHeaderRow(sheet, workbook);
            }

            int lastRowNum = sheet.getLastRowNum();
            int dataStartRow = findDataStartRow(sheet);
            int nextRow = dataStartRow == -1 ? sheet.getLastRowNum() + 1 : lastRowNum + 1;

            CellStyle passStyle = workbook.createCellStyle();
            passStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            passStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle failStyle = workbook.createCellStyle();
            failStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (TestResult result : results) {
                Row row = sheet.createRow(nextRow++);
                row.createCell(0).setCellValue(result.testCaseId);
                row.createCell(1).setCellValue(result.testCaseName);
                row.createCell(2).setCellValue(result.duration);
                row.createCell(3).setCellValue(result.executedBy);

                Cell statusCell = row.createCell(4);
                statusCell.setCellValue(result.status);
                statusCell.setCellStyle(result.status.equalsIgnoreCase("Pass") ? passStyle : failStyle);

                row.createCell(5).setCellValue(result.errorMessage == null ? "" : result.errorMessage);

                if (result.screenshotPath != null) {
                    try (InputStream inputStream = new FileInputStream(result.screenshotPath)) {
                        byte[] imageBytes = IOUtils.toByteArray(inputStream);
                        int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
                        Drawing<?> drawing = sheet.createDrawingPatriarch();
                        CreationHelper helper = workbook.getCreationHelper();
                        ClientAnchor anchor = helper.createClientAnchor();
                        anchor.setCol1(6);
                        anchor.setRow1(row.getRowNum());
                        Picture pict = drawing.createPicture(anchor, pictureIdx);
                        pict.resize(1.0);
                    }
                }
            }

            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            updateSummaryBlock(sheet, workbook);

            FileOutputStream fos = new FileOutputStream(REPORT_PATH);
            workbook.write(fos);
            workbook.close();
            fos.close();

            System.out.println("Excel report updated: " + REPORT_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int findDataStartRow(Sheet sheet) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && row.getCell(0) != null &&
                "TestCase ID".equalsIgnoreCase(row.getCell(0).getStringCellValue())) {
                return i + 1;
            }
        }
        return -1;
    }

    private static void addHeaderRow(Sheet sheet, Workbook workbook) {
        Row headerRow = sheet.createRow(sheet.getLastRowNum() + 1);
        String[] headers = {
                "TestCase ID", "TestCase Name", "Duration (s)", "Executed By", "Status", "Error Message", "Screenshot"
        };

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private static void addSummaryBlock(Sheet sheet, Workbook workbook) {
        for (int i = 0; i < 6; i++) {
            sheet.createRow(i);
        }
    }

    private static void updateSummaryBlock(Sheet sheet, Workbook workbook) {
        int totalCases = results.size();
        int passed = (int) results.stream().filter(r -> r.status.equalsIgnoreCase("Pass")).count();
        int failed = totalCases - passed;
        int totalTime = (int) results.stream().mapToDouble(r -> r.duration).sum();

        String env = detectEnvironment();
        String featureNames = String.join(", ", executedFeatures);

        String[] lines = {
                "SERVER: " + env,
                "FEATURES RUNNED: " + executedFeatures.size(),
                "FEATURE FILES: " + featureNames,
                "TOTAL TEST CASE EXECUTED : " + totalCases,
                "TOTAL PASSED : " + passed,
                "TOTAL FAILED : " + failed,
                "TOTAL TIME EXECUTED : " + totalTime + " sec"
        };

        Font font = workbook.createFont();
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);

        for (int i = 0; i < lines.length; i++) {
            Row row = sheet.getRow(i);
            if (row == null) row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(lines[i]);
            cell.setCellStyle(style);
        }
    }

    private static String detectEnvironment() {
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            Properties prop = new Properties();
            prop.load(input);
            String url = prop.getProperty("url");
            if (url.contains("qa.zlta.testingserver8.com")) return "QA";
            if (url.contains("testing.zlaata.com")) return "Staging";
            if (url.contains("www.zlaata.com")) return "Production";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }
}
