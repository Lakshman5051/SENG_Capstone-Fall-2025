package com.project.library_comparison_tool.Controller;
import com.project.library_comparison_tool.service.LibrariesIoDataLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/data")

/** This method is to load data from libraries.io
   * for this i have created a controller
 */
public class LibraryAPIDataController {

        private final LibrariesIoDataLoader dataLoader ;

        public LibraryAPIDataController(LibrariesIoDataLoader dataLoader) {
            this.dataLoader = dataLoader;
        }

        /**
         * This method is for loading the data,(parameters:query,platform,pages)
         * doc link check : https://libraries.io/api
         * POST /api/admin/data/load?query=json&platform=Maven&pages=2
         */
        @PostMapping("/load")
        public ResponseEntity<Map<String, Object>> loadLibraries(
                @RequestParam String query,
                @RequestParam String platform,
                @RequestParam(defaultValue = "1") int pages) {

            int count = dataLoader.loadLibraries(query, platform, pages);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("query", query);
            response.put("platform", platform);
            response.put("pagesLoaded", pages);
            response.put("librariesLoaded", count);

            return ResponseEntity.ok(response);
        }

        /**
         * Load popular libraries
         * POST /api/admin/data/load-popular
         */
        @PostMapping("/load-popular")
        public ResponseEntity<Map<String, Object>> loadPopularLibraries() {

            int count = dataLoader.loadPopularLibraries();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("librariesLoaded", count);
            response.put("message", "Loaded popular libraries across multiple platforms");

            return ResponseEntity.ok(response);
        }
    }
