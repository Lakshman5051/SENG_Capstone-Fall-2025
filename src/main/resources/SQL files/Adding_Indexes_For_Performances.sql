-- Essential indexes for search and sorting
CREATE INDEX idx_library_name ON library(name);
CREATE INDEX idx_library_category ON library(category);
CREATE INDEX idx_library_stars ON library(github_stars DESC NULLS LAST);
CREATE INDEX idx_library_downloads ON library(downloads_last30days DESC NULLS LAST);

-- Composite indexes for common queries
CREATE INDEX idx_category_stars ON library(category, github_stars DESC NULLS LAST);
CREATE INDEX idx_language_category ON library(language, category);