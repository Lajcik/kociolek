package org.lajcik.kociolek.service;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * User: sienkom
 */
public interface ReportService {
    void generateReport(File file, Calendar dateFrom, Calendar dateTo) throws IOException;
}
