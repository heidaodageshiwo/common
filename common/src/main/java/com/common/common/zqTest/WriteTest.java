package com.common.common.zqTest;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.CommentData;
import com.alibaba.excel.metadata.data.FormulaData;
import com.alibaba.excel.metadata.data.HyperlinkData;
import com.alibaba.excel.metadata.data.HyperlinkData.HyperlinkType;
import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.ImageData.ImageType;
import com.alibaba.excel.metadata.data.RichTextStringData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.BooleanUtils;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;

/**
 * 写的常见写法
 *
 * @author Jiaju Zhuang
 */
public class WriteTest {
    public static void main(String[] args) {
        write();;
    }

    private static void write() {
        //地址  https://www.jianshu.com/p/10e05e89b8a0
        //地址  https://www.jianshu.com/p/10e05e89b8a0
        //地址  https://www.jianshu.com/p/10e05e89b8a0
        List list = new ArrayList();
        list.add(new Question1("1", "1", "1"));
        list.add(new Question1("2", "1", "1"));
        list.add(new Question1("3", "1", "1"));
        EasyExcel
                .write("E:\\IDEA\\idea_project_180214\\20211118_common_java\\common1\\common\\src\\main\\resources\\excel/test_w.xlsx", Question1.class)
                .sheet()
                .doWrite(list);


        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write("E:\\IDEA\\idea_project_180214\\20211118_common_java\\common1\\common\\src\\main\\resources\\excel/test_w.xlsx", Question1.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            excelWriter.write(list, writeSheet);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }
}
