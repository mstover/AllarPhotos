package com.lazerinc.utils;

public enum Right {
	READ {
		public String value() {
			return "read";
		}
	},
	ORDER {
		public String value() {
			return "order";
		}
	},
	DOWNLOAD_ORIG {
		public String value() {
			return "download_orig";
		}
	},
	DOWNLOAD {
		public String value() {
			return "download";
		}
	},
	ADMIN {
		public String value() {
			return "admin";
		}
	},
	UPLOAD {
		public String value() {
			return "upload";
		}
	},
	NONE {
		public String value() {
			return "NONE";
		}
	};

	public abstract String value();

	public String toString() {
		return value();
	}

	public static Right getRight(String right) {
		if (READ.value().equals(right))
			return READ;
		else if (ORDER.value().equals(right))
			return ORDER;
		else if (DOWNLOAD.value().equals(right))
			return DOWNLOAD;
		else if (DOWNLOAD_ORIG.value().equals(right))
			return DOWNLOAD_ORIG;
		else if (ADMIN.value().equals(right))
			return ADMIN;
		else if (UPLOAD.value().equals(right))
			return UPLOAD;
		else
			return NONE;
	}

}
