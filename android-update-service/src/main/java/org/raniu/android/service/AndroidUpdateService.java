package org.raniu.android.service;

import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.vo.Result;

public interface AndroidUpdateService {

    Result<String> getVersion(String name, HttpServletResponse response);

    Result<String> getApk(String name, String version, HttpServletResponse response);
}
