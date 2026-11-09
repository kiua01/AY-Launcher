//! AY Launcher Core (Rust)
//! 
//! Handles high-performance operations:
//! - Parallel asset / version downloads
//! - Hash verification
//! - Launch argument construction helpers
//! - Future: process supervision

use serde::{Deserialize, Serialize};

#[derive(Debug, Serialize, Deserialize)]
pub struct DownloadTask {
    pub url: String,
    pub path: String,
    pub sha1: Option<String>,
}

#[derive(Debug, thiserror::Error)]
pub enum CoreError {
    #[error("Network error: {0}")]
    Network(String),
    #[error("IO error: {0}")]
    Io(String),
    #[error("Hash mismatch")]
    HashMismatch,
}

/// Placeholder for future UniFFI / JNI exports.
pub fn version() -> &'static str {
    "0.1.0-ay-core"
}

/// Example async download entry (to be wired to Kotlin later)
pub async fn download_file(task: DownloadTask) -> Result<(), CoreError> {
    // Implementation will use reqwest + tokio + sha2 verification
    let _ = task;
    Ok(())
}
