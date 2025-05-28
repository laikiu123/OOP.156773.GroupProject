// File: SVBK/model/ProgramType.java
package SVBK.model;

/**
 * Kiểu chương trình đào tạo, dùng để phân biệt chương trình chỉ dành cho sinh
 * viên hệ tín chỉ (Credit-based) hay sinh viên hệ niên chế (Part-time).
 */
public enum ProgramType {
	/**
	 * Chương trình đào tạo dành cho sinh viên hệ tín chỉ. Sinh viên đăng ký tự
	 * chọn, tích lũy tín chỉ, và tốt nghiệp khi đủ tín chỉ theo yêu cầu chương
	 * trình.
	 */
	CREDIT_BASED,

	/**
	 * Chương trình đào tạo dành cho sinh viên hệ niên chế. Sinh viên học theo danh
	 * sách cố định, không đăng ký tự chọn, và tốt nghiệp khi hoàn thành tất cả học
	 * phần với điểm trung bình tối thiểu đạt ngưỡng.
	 */
	PART_TIME
}
