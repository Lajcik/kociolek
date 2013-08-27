package org.lajcik.kociolek.service;

import org.lajcik.kociolek.dao.RentalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Calendar;
import java.util.List;

/**
 * User: sienkom
 */
@Component
@Transactional
public class ReportServiceImpl implements ReportService {

    private static final String SEPARATOR = ";";
    @Autowired
    private RentalDao rentalDao;

    @Override
    public void generateReport(File file, Calendar dateFrom, Calendar dateTo)
            throws IOException {
        adjust(dateFrom, dateTo);

        if(!file.createNewFile()) {
            throw new IllegalStateException("Could not create file " + file);
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file))));

            List<Object[]> data = rentalDao.getReportData(dateFrom.getTime(), dateTo.getTime());

            writeHeader(out);
            for (Object[] row : data) {
                String name = (String) row[0];
                Long rentCount = (Long) row[1];
                Long rentMinutes = (Long) row[2];

                writeRow(out, name, rentCount, rentMinutes);
            }

        } finally {
            if(out != null) {
                out.close();
            }
        }
    }

    private void writeHeader(PrintWriter out) {
        out.print("Nazwa");
        out.print(SEPARATOR);
        out.print("Ilość wypożyczeń");
        out.print(SEPARATOR);
        out.print("Łączny czas (w minutach)");
        out.println();
    }

    private void writeRow(PrintWriter out, String name, Long rentCount, Long rentMinutes) {
        out.print(name);
        out.print(SEPARATOR);
        out.print(rentCount);
        out.print(SEPARATOR);
        out.print(rentMinutes);
        out.println();
    }

    private void adjust(Calendar dateFrom, Calendar dateTo) {
        dateFrom.set(Calendar.HOUR, 0);
        dateFrom.set(Calendar.MINUTE, 0);
        dateFrom.set(Calendar.SECOND, 0);
        dateFrom.set(Calendar.MILLISECOND, 0);

        dateTo.set(Calendar.HOUR, 23);
        dateTo.set(Calendar.MINUTE, 59);
        dateTo.set(Calendar.SECOND, 59);
        dateTo.set(Calendar.MILLISECOND, 59);
    }
}
