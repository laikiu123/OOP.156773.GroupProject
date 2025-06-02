package utils;

import model.Course;
import model.Program;
import model.ProgramType;
import java.util.ArrayList;
// import java.util.Arrays; // Arrays was in old data, but not used by new methods or createCoursesOnly/findCourseById
import java.util.List;

public class SampleDataGenerator {

    public static List<Course> createCoursesOnly() {
        List<Course> allCourses = new ArrayList<>();

        // Học phần tự chọn (Thể dục)
        allCourses.add(new Course("PE1015", "Thể dục tay không", 2, null, 0.50));
        allCourses.add(new Course("PE1024", "Bơi lội", 2, null, 0.50));
        allCourses.add(new Course("PE2101", "Bóng chuyền", 2, null, 0.50));
        allCourses.add(new Course("PE2151", "Erobic", 2, null, 0.50));
        allCourses.add(new Course("PE2201", "Bóng đá", 2, null, 0.50));
        allCourses.add(new Course("PE2251", "Taekwondo", 2, null, 0.50));
        allCourses.add(new Course("PE2261", "Karatedo", 2, null, 0.50));
        allCourses.add(new Course("PE2301", "Bóng rổ", 2, null, 0.50));
        allCourses.add(new Course("PE2401", "Bóng bàn", 2, null, 0.50));
        allCourses.add(new Course("PE2501", "Cầu lông", 2, null, 0.50));
        allCourses.add(new Course("PE2601", "Chạy", 2, null, 0.50));
        allCourses.add(new Course("PE2701", "Nhảy cao", 2, null, 0.50));
        allCourses.add(new Course("PE2801", "Nhảy xa", 2, null, 0.50));


        // Học phần đại cương
        allCourses.add(new Course("MI1114", "Giải tích I", 3, null, 0.50));
        allCourses.add(new Course("MI1124", "Giải tích II", 3, "MI1114", 0.50));
        allCourses.add(new Course("MI1134", "Giải tích III", 3, "MI1124", 0.50));
        allCourses.add(new Course("MI1144", "Đại số tuyến tính", 3, null, 0.50));
        allCourses.add(new Course("MI2021", "Xác suất thống kê", 3, "MI1114", 0.50));
        allCourses.add(new Course("EM1170", "Pháp luật đại cương", 2, null, 0.50));
        allCourses.add(new Course("SSH1111", "Triết học Mác Lênin", 3, null, 0.50));
        allCourses.add(new Course("SSH1121", "Kinh tế chính trị Mác Lênin", 2, "SSH1111", 0.50));
        allCourses.add(new Course("SSH1131", "Chủ nghĩa xã hội khoa học", 2, null, 0.50));
        allCourses.add(new Course("SSH1141", "Lịch sử Đảng cộng sản Việt Nam", 2, "SSH1131", 0.50)); // Lịch sử ĐCSVN
        allCourses.add(new Course("SSH1151", "Tư tưởng Hồ Chí Minh", 2, null, 0.50));

        // Công nghệ thông tin Việt Nhật (IT-E6)
        allCourses.add(new Course("IT2110", "Nhập môn CNTT và TT", 2, null, 0.50));
        allCourses.add(new Course("IT2120", "Kiến trúc máy tính", 2, null, 0.70)); //IT-E6 dùng cái này
        allCourses.add(new Course("IT3022", "Toán rời rạc", 3, null, 0.50));
        allCourses.add(new Course("IT3072", "Hệ điều hành", 2, null, 0.70));
        allCourses.add(new Course("IT3082", "Mạng máy tính", 3, null, 0.70));
        allCourses.add(new Course("IT3210", "C Programming Language", 2, null, 0.50));
        allCourses.add(new Course("T3220", "C Programming (Introduction)", 2, null, 0.50)); // C Programming (Intro)
        allCourses.add(new Course("T3260", "Lý thuyết mạch logic", 3, null, 0.60));
        allCourses.add(new Course("T3270", "Thực hành mạch logic", 2, null, 0.70));
        allCourses.add(new Course("IT3280", "Thực hành kiến trúc máy tính", 2, null, 0.60)); // TH kiến trúc máy tính
        allCourses.add(new Course("IT3282", "Kiến trúc máy tính", 3, null, 0.50)); // Khác IT2120
        allCourses.add(new Course("T3290", "Thực hành cơ sở dữ liệu", 3, null, 0.60)); // TH cơ sở dữ liệu
        allCourses.add(new Course("IT3292", "Cơ sở dữ liệu", 2, null, 0.70));
        allCourses.add(new Course("IT3312", "Cấu trúc dữ liệu và giải thuật", 2, null, 0.70)); // Cấu trúc dữ liệu & giải thuật
        allCourses.add(new Course("IT4082", "Kỹ thuật phần mềm", 3, null, 0.50));
        allCourses.add(new Course("IT4652", "Mạng Internet", 2, null, 0.70));
        allCourses.add(new Course("IT3250", "Đạo đức máy tính", 3, null, 0.50));
        allCourses.add(new Course("IT3103", "Lập trình hướng đối tượng", 2, null, 0.60));
        allCourses.add(new Course("IT4312", "Mô hình hóa dữ liệu", 2, null, 0.60));
        allCourses.add(new Course("IT4262", "Bảo mật mạng máy tính", 3, null, 0.70));

        // Kỹ thuật hóa học (CH1)
        allCourses.add(new Course("CH2000", "Nhập môn Kỹ thuật Hóa học", 3, null, 0.50));
        allCourses.add(new Course("CH3120", "Hóa vô cơ", 2, null, 0.70));
        allCourses.add(new Course("CH3130", "Thí nghiệm Hóa vô cơ", 3, null, 0.50));
        allCourses.add(new Course("CH3220", "Hóa hữu cơ", 3, null, 0.70));
        allCourses.add(new Course("CH3230", "Thí nghiệm Hóa hữu cơ", 2, null, 0.50));
        allCourses.add(new Course("CH3051", "Hóa lý I", 2, null, 0.70));
        allCourses.add(new Course("CH3052", "Thí nghiệm Hóa lý I", 2, null, 0.50));
        allCourses.add(new Course("CH3061", "Hóa lý II", 3, null, 0.70));
        allCourses.add(new Course("CH3062", "Thí nghiệm Hóa lý II", 2, null, 0.50));
        allCourses.add(new Course("CH3330", "Hóa phân tích", 3, null, 0.70)); // CH1 dùng cái này
        allCourses.add(new Course("CH3340", "Thí nghiệm Hóa phân tích", 2, null, 0.50)); // CH1 dùng cái này
        allCourses.add(new Course("CH3323", "Phương pháp phân tích bằng công cụ", 2, null, 0.60)); // PP phân tích bằng công cụ
        allCourses.add(new Course("CH3324", "Thực hành phân tích bằng công cụ", 3, null, 0.60)); // TH phân tích bằng công cụ
        allCourses.add(new Course("CH3400", "Quá trình và Thiết bị CN Hóa học 1 (Các quá trình thủy lực và thủy cơ)", 2, null, 0.70)); // QTTB CN Hóa học 1
        allCourses.add(new Course("CH3412", "Quá trình và Thiết bị CN Hóa học 2 (Các quá trình nhiệt)", 2, null, 0.70)); // QTTB CN Hóa học 2
        allCourses.add(new Course("CH3420", "Quá trình và Thiết bị CN Hóa học 3 (Các quá trình chuyển khối)", 2, null, 0.70)); // QTTB CN Hóa học 3
        allCourses.add(new Course("CH3480", "Thí nghiệm QTTB I", 2, null, 0.50));
        allCourses.add(new Course("CH3490", "Thí nghiệm QTTB II", 2, null, 0.50));
        allCourses.add(new Course("CH3900", "Đồ án QTTB", 3, null, 0.60));
        allCourses.add(new Course("EE2090", "Kỹ thuật Điện và Điều khiển quá trình", 3, null, 0.50)); // Kỹ thuật Điện và ĐK quá trình
        allCourses.add(new Course("CH3452", "Mô phỏng trong Công nghệ hóa học", 2, null, 0.70)); // Mô phỏng trong CN Hóa học
        allCourses.add(new Course("CH3700", "Cơ khí ứng dụng trong kỹ thuật hóa học", 2, null, 0.60)); // Cơ khí ứng dụng
        allCourses.add(new Course("CH3800", "Xây dựng công nghiệp", 2, null, 0.60));

        // Kỹ thuật Sinh học (BF1)
        allCourses.add(new Course("CH3316", "Hóa phân tích", 3, null, 0.70)); // BF1 dùng cái này
        allCourses.add(new Course("CH3318", "Thí nghiệm hóa phân tích", 3, null, 0.50)); // BF1 dùng cái này
        allCourses.add(new Course("EE2012", "Kỹ thuật điện", 2, null, 0.70)); // Dùng cho BF1 (2 tín chỉ) và PH1 (cần đổi ID cho PH1)
        allCourses.add(new Course("ME2015", "Đồ họa kỹ thuật cơ bản", 3, null, 0.60));
        allCourses.add(new Course("BF2701", "Nhập môn kỹ thuật sinh học", 2, null, 0.70));
        allCourses.add(new Course("BF3711", "Quá trình và thiết bị CNSH I", 2, null, 0.50));
        allCourses.add(new Course("BF3712", "Quá trình và thiết bị CNSH II", 2, null, 0.50));
        allCourses.add(new Course("BF3713", "Quá trình và thiết bị CNSH III", 2, null, 0.50));
        allCourses.add(new Course("BF4725", "Kỹ thuật đo lường và điều khiển tự động trong CNSH", 2, null, 0.70));
        allCourses.add(new Course("BF4726", "Quản lý chất lượng trong CNSH", 2, null, 0.70));
        allCourses.add(new Course("BF3714", "Đồ án quá trình và thiết bị CNSH", 2, null, 0.70));
        allCourses.add(new Course("BF2702", "Hóa sinh", 3, null, 0.70));
        allCourses.add(new Course("BF2703", "Thí nghiệm hóa sinh", 3, null, 0.50));
        allCourses.add(new Course("BF3701", "Vi sinh vật I", 2, null, 0.70));
        allCourses.add(new Course("BF3702", "Thí nghiệm vi sinh vật", 2, null, 0.50));
        allCourses.add(new Course("BF3703", "Sinh học tế bào", 3, null, 0.60));
        allCourses.add(new Course("BF3704", "Miễn dịch học", 2, null, 0.70));
        allCourses.add(new Course("BF3705", "Di truyền học và Sinh học phân tử", 2, null, 0.70));
        allCourses.add(new Course("BF3706", "Kỹ thuật gen", 3, null, 0.60));
        allCourses.add(new Course("BF3707", "Tin sinh học", 2, null, 0.70));
        allCourses.add(new Course("BF3708", "Phương pháp phân tích trong CNSH", 2, null, 0.50));
        allCourses.add(new Course("BF4727", "Đồ án chuyên ngành KTSH", 2, null, 0.70));


        // Vật lý kỹ thuật (PH1)
        allCourses.add(new Course("PH2010", "Nhập môn Vật lý kỹ thuật", 3, null, 0.50));
        // SỬA LỖI ID TRÙNG: Đổi "EE2012" thành "EE2012PH" cho ngành Vật Lý Kỹ Thuật (từ dữ liệu cũ)
        // Trong dữ liệu mới, "EE2012" được dùng chung. Nếu PH1 dùng bản khác, ID phải khác.
        // Giả sử PH1 dùng EE2012 3 tín chỉ như cũ, trong khi BF1 dùng EE2012 2 tín chỉ.
        // Nếu EE2012 là duy nhất và PH1 dùng nó, thì phải thống nhất tín chỉ hoặc có môn riêng.
        // Dữ liệu mới cho generateEngineeringPhysicsProgram() gọi "EE2012".
        // allCourses.add(new Course("EE2012PH", "Kỹ thuật điện (VLKT)", 3, null, 0.70)); // ID đã sửa, tên để phân biệt (từ dữ liệu cũ)
        allCourses.add(new Course("ME2115", "Vẽ kỹ thuật trên máy tính", 2, null, 0.50));
        allCourses.add(new Course("PH3010", "Phương pháp toán cho vật lý", 2, null, 0.60)); // PP toán cho vật lý
        allCourses.add(new Course("ET2010", "Kỹ thuật điện tử", 3, null, 0.70));
        allCourses.add(new Course("PH2021", "Đồ án môn học I", 2, null, 0.70));
        allCourses.add(new Course("PH3350", "Căn bản khoa học máy tính cho kỹ sư vật lý", 2, null, 0.70)); // Căn bản KH máy tính cho kỹ sư vật lý
        allCourses.add(new Course("PH3060", "Cơ học lượng tử", 3, null, 0.60));
        allCourses.add(new Course("PH3030", "Trường điện từ", 2, null, 0.70));
        allCourses.add(new Course("PH3400", "Cơ sở quang học, quang ĐT", 2, null, 0.70)); // Cơ sở quang học, quang điện tử
        allCourses.add(new Course("PH3110", "Vật lý chất rắn", 3, null, 0.50));
        allCourses.add(new Course("PH3120", "Vật lý thống kê", 3, null, 0.50));
        allCourses.add(new Course("PH3360", "Tính toán trong vật lý và khoa học vật liệu", 2, null, 0.70)); // Tính toán trong vật lý và KH vật liệu
        allCourses.add(new Course("PH3071", "Vật lý và kỹ thuật chân không", 2, null, 0.60));
        allCourses.add(new Course("PH2022", "Đồ án môn học II", 2, null, 0.60));
        allCourses.add(new Course("PH3190", "Vật lý và linh kiện bán dẫn", 2, null, 0.70));
        allCourses.add(new Course("PH3410", "Hệ thống nhúng và ứng dụng", 2, null, 0.70));
        allCourses.add(new Course("PH3330", "Vật lý điện tử", 3, null, 0.50));
        allCourses.add(new Course("PH4060", "Công nghệ vật liệu", 3, null, 0.50));
        allCourses.add(new Course("PH4490", "Kỹ thuật xử lý ảnh và ứng dụng trong kỹ thuật", 2, null, 0.70)); // Kỹ thuật xử lý ảnh và ứng dụng
        allCourses.add(new Course("PH3090", "Quang học kỹ thuật", 2, null, 0.60));
        allCourses.add(new Course("PH4600", "Cơ sở kỹ thuật ánh sáng", 2, null, 0.60));

        // Quản trị kinh doanh (EM3)
        allCourses.add(new Course("EM3310", "Mô phỏng hoạt động kinh doanh", 3, null, 0.50));
        allCourses.add(new Course("EM3500", "Nguyên lý kế toán", 2, null, 0.50));
        allCourses.add(new Course("EM3519", "Tài chính doanh nghiệp", 3, null, 0.70));
        allCourses.add(new Course("EM3600", "Phân tích dữ liệu và Trí tuệ kinh doanh", 2, null, 0.70));
        allCourses.add(new Course("EM4212", "Phân tích kinh doanh", 3, null, 0.50));
        allCourses.add(new Course("EM4218", "Hệ thống thông tin quản lý", 3, null, 0.70));
        allCourses.add(new Course("EM4416", "Quản trị chiến lược (BTL)", 2, null, 0.70));
        allCourses.add(new Course("EM3432", "Quản trị chuỗi cung ứng (BTL)", 2, null, 0.70));
        allCourses.add(new Course("EM4716", "Kế toán quản trị", 2, null, 0.70));
        allCourses.add(new Course("EM3160", "Tâm lý học quản lý", 3, null, 0.50));
        allCourses.add(new Course("EM3301", "Đạo đức kinh doanh", 3, null, 0.70));
        allCourses.add(new Course("EM4201", "Quan hệ lao động", 2, null, 0.70));
        allCourses.add(new Course("EM4202", "Tiền lương, phúc lợi và BHXH", 2, null, 0.50));
        allCourses.add(new Course("EM4210", "Khởi sự kinh doanh (BTL)", 2, null, 0.70));
        allCourses.add(new Course("EM4437", "Định mức lao động", 3, null, 0.60));
        allCourses.add(new Course("EM4336", "Thương mại điện tử", 2, null, 0.70));
        allCourses.add(new Course("EM4216", "PP nghiên cứu trong kinh doanh", 2, null, 0.60));
        allCourses.add(new Course("EM4314", "Hành vi người tiêu dùng", 2, null, 0.50));
        allCourses.add(new Course("EM4332", "Quản trị thương hiệu", 3, null, 0.70));
        allCourses.add(new Course("EM4435", "Quản trị dự án", 2, null, 0.60));

        return allCourses;
    }

    public static Course findCourseById(List<Course> courses, String id) {
        for (Course c : courses) {
            if (c.getCourseID().equals(id)) return c;
        }
        System.err.println("Lỗi tìm Course by ID: Không tìm thấy môn với ID '" + id + "' trong danh sách cung cấp.");
        return null;
    }

    public static Program generateVietNhatITparttimeProgram() {
        List<Course> all_course = createCoursesOnly();
        List<Course> required = new ArrayList<>();

        // --- Các môn đại cương  ---
        required.add(findCourseById(all_course, "MI1114"));  // Giải tích I
        required.add(findCourseById(all_course, "MI1144"));  // Đại số tuyến tính
        required.add(findCourseById(all_course, "MI2021"));  // Xác suất thống kê
        required.add(findCourseById(all_course, "EM1170"));  // Pháp luật đại cương
        required.add(findCourseById(all_course, "SSH1111")); // Triết học Mác Lênin
        required.add(findCourseById(all_course, "SSH1131")); // Chủ nghĩa xã hội khoa học

        // --- Các môn Việt Nhật CNTT (IT-E6)  ---
        required.add(findCourseById(all_course, "IT2110"));  // Nhập môn CNTT và TT
        required.add(findCourseById(all_course, "IT2120"));  // Kiến trúc máy tính
        required.add(findCourseById(all_course, "IT3022"));  // Toán rời rạc
        required.add(findCourseById(all_course, "IT3210"));  // C Programming Language
        required.add(findCourseById(all_course, "IT3312"));  // Cấu trúc dữ liệu & giải thuật
        required.add(findCourseById(all_course, "IT3292"));  // Cơ sở dữ liệu
        required.add(findCourseById(all_course, "IT3072"));  // Hệ điều hành
        required.add(findCourseById(all_course, "IT3082"));  // Mạng máy tính
        required.add(findCourseById(all_course, "IT4082"));  // Kỹ thuật phần mềm
        required.add(findCourseById(all_course, "IT3103"));  // Lập trình hướng đối tượng

        // required.removeIf(course -> course == null); // Consider adding null checks if findCourseById can return null and it's not handled

        List<Course> electiveCourses = new ArrayList<>(); // Không có môn tự chọn cho niên chế
        int electiveCreditRequirement = 0;

        // Tính tổng tín chỉ yêu cầu tự động
        int totalCreditRequirement = 0;
        for (Course c : required) {
            if (c != null) { // Check for null before accessing getCreditCount()
                totalCreditRequirement += c.getCreditCount();
            }
        }

        Program vietNhatITPartTime = new Program(
            "IT-E6-NC",
            ProgramType.PART_TIME,
            required,
            electiveCourses,
            electiveCreditRequirement,
            totalCreditRequirement
        );

        return vietNhatITPartTime;
    }

    public static Program generateChemicalEngineeringPartTimeProgram() {
        List<Course> all_course = createCoursesOnly();
        List<Course> required = new ArrayList<>();

        // --- Các môn đại cương  ---
        required.add(findCourseById(all_course, "MI1114"));  // Giải tích I
        required.add(findCourseById(all_course, "MI1144"));  // Đại số tuyến tính
        required.add(findCourseById(all_course, "MI2021"));  // Xác suất thống kê
        required.add(findCourseById(all_course, "EM1170"));  // Pháp luật đại cương
        required.add(findCourseById(all_course, "SSH1111")); // Triết học Mác Lênin
        required.add(findCourseById(all_course, "SSH1131")); // Chủ nghĩa xã hội khoa học

        // --- Các môn Kỹ thuật hóa học (CH1) ---
        required.add(findCourseById(all_course, "CH2000")); // Nhập môn Kỹ thuật Hóa học
        required.add(findCourseById(all_course, "CH3120")); // Hóa vô cơ
        required.add(findCourseById(all_course, "CH3220")); // Hóa hữu cơ
        required.add(findCourseById(all_course, "CH3051")); // Hóa lý I
        required.add(findCourseById(all_course, "CH3061")); // Hóa lý II
        required.add(findCourseById(all_course, "CH3330")); // Hóa phân tích
        required.add(findCourseById(all_course, "CH3400")); // QTTB CN Hóa học 1
        required.add(findCourseById(all_course, "CH3412")); // QTTB CN Hóa học 2
        required.add(findCourseById(all_course, "CH3420")); // QTTB CN Hóa học 3
        required.add(findCourseById(all_course, "EE2090")); // Kỹ thuật Điện và ĐK quá trình
        required.add(findCourseById(all_course, "CH3900")); // Đồ án QTTB
        required.add(findCourseById(all_course, "CH3452")); // Mô phỏng trong CN Hóa học

        // required.removeIf(course -> course == null); // Consider adding null checks

        List<Course> electiveCourses = new ArrayList<>();
        int electiveCreditRequirement = 0;

        int totalCreditRequirement = 0;
        for (Course c : required) {
            if (c != null) { // Check for null
                totalCreditRequirement += c.getCreditCount();
            }
        }

        Program chemicalEngineeringPartTime = new Program(
            "CH1-NC",
            ProgramType.PART_TIME,
            required,
            electiveCourses,
            electiveCreditRequirement,
            totalCreditRequirement
        );

        return chemicalEngineeringPartTime;
    }

    public static Program generateVietNhatITProgram() {
        List<Course> all_course = createCoursesOnly();
        List<Course> required = new ArrayList<>();

        // --- Các môn đại cương ---
        required.add(findCourseById(all_course, "MI1114"));  // Giải tích I
        required.add(findCourseById(all_course, "MI1124"));  // Giải tích II
        required.add(findCourseById(all_course, "MI1134"));  // Giải tích III
        required.add(findCourseById(all_course, "MI1144"));  // Đại số tuyến tính
        required.add(findCourseById(all_course, "MI2021"));  // Xác suất thống kê
        required.add(findCourseById(all_course, "EM1170"));  // Pháp luật đại cương
        required.add(findCourseById(all_course, "SSH1111")); // Triết học Mác Lênin
        required.add(findCourseById(all_course, "SSH1121")); // Kinh tế chính trị Mác Lênin
        required.add(findCourseById(all_course, "SSH1131")); // Chủ nghĩa xã hội khoa học
        required.add(findCourseById(all_course, "SSH1141")); // Lịch sử ĐCSVN
        required.add(findCourseById(all_course, "SSH1151")); // Tư tưởng Hồ Chí Minh

        // --- Các môn Việt Nhật CNTT (IT-E6) ---
        required.add(findCourseById(all_course, "IT2110"));  // Nhập môn CNTT và TT
        required.add(findCourseById(all_course, "IT2120"));  // Kiến trúc máy tính
        required.add(findCourseById(all_course, "IT3022"));  // Toán rời rạc
        required.add(findCourseById(all_course, "IT3072"));  // Hệ điều hành
        required.add(findCourseById(all_course, "IT3082"));  // Mạng máy tính
        required.add(findCourseById(all_course, "IT3210"));  // C Programming Language
        required.add(findCourseById(all_course, "T3220"));   // C Programming (Intro)
        required.add(findCourseById(all_course, "T3260"));   // Lý thuyết mạch logic
        required.add(findCourseById(all_course, "T3270"));   // Thực hành mạch logic
        required.add(findCourseById(all_course, "IT3280"));  // TH kiến trúc máy tính
        required.add(findCourseById(all_course, "IT3282"));  // Kiến trúc máy tính
        required.add(findCourseById(all_course, "T3290"));   // TH cơ sở dữ liệu
        required.add(findCourseById(all_course, "IT3292"));  // Cơ sở dữ liệu
        required.add(findCourseById(all_course, "IT3312"));  // Cấu trúc dữ liệu & giải thuật
        required.add(findCourseById(all_course, "IT4082"));  // Kỹ thuật phần mềm
        required.add(findCourseById(all_course, "IT4652"));  // Mạng Internet
        required.add(findCourseById(all_course, "IT3250"));  // Đạo đức máy tính
        required.add(findCourseById(all_course, "IT3103"));  // Lập trình hướng đối tượng
        required.add(findCourseById(all_course, "IT4312"));  // Mô hình hóa dữ liệu
        required.add(findCourseById(all_course, "IT4262"));  // Bảo mật mạng máy tính

        // required.removeIf(course -> course == null); // Consider adding null checks

        List<Course> elective = new ArrayList<>();

        // --- Các môn học phần tự chọn: Thể dục ---
        elective.add(findCourseById(all_course, "PE1015")); // Thể dục tay không
        elective.add(findCourseById(all_course, "PE1024")); // Bơi lội
        elective.add(findCourseById(all_course, "PE2101")); // Bóng chuyền
        elective.add(findCourseById(all_course, "PE2151")); // Erobic
        elective.add(findCourseById(all_course, "PE2201")); // Bóng đá
        elective.add(findCourseById(all_course, "PE2251")); // Taekwondo
        elective.add(findCourseById(all_course, "PE2261")); // Karatedo
        elective.add(findCourseById(all_course, "PE2301")); // Bóng rổ
        elective.add(findCourseById(all_course, "PE2401")); // Bóng bàn
        elective.add(findCourseById(all_course, "PE2501")); // Cầu lông
        elective.add(findCourseById(all_course, "PE2601")); // Chạy
        elective.add(findCourseById(all_course, "PE2701")); // Nhảy cao
        elective.add(findCourseById(all_course, "PE2801")); // Nhảy xa

        // elective.removeIf(course -> course == null); // Consider adding null checks

        int totalCreditRequirement = 0;
        for (Course c : required) {
            if (c != null) { // Check for null
                 totalCreditRequirement += c.getCreditCount();
            }
        }

        // Ví dụ ngưỡng tín chỉ tự chọn và tổng tín chỉ yêu cầu
        int electiveCreditRequirement = 12; // This is the requirement, not the sum of elective list credits
        // totalCreditRequirement should be sum of required credits + electiveCreditRequirement
        // The sum of actual elective courses in the 'elective' list should be >= electiveCreditRequirement
        // For now, let's assume totalCreditRequirement means sum_of_required_credits + elective_credit_requirement
        // This matches the new logic provided:
        // totalCreditRequirement += electiveCreditRequirement;
        // However, the Program constructor expects the *final* total.
        // The initial loop calculates sum of required. Then you add electiveCreditRequirement to it.

        // Re-calculating totalCreditRequirement as sum of required credits only first
        int sumRequiredCredits = 0;
        for (Course c : required) {
            if (c != null) {
                sumRequiredCredits += c.getCreditCount();
            }
        }
        // The total for the program is sum of required credits + number of elective credits student *must* take
        totalCreditRequirement = sumRequiredCredits + electiveCreditRequirement;


        Program vietNhatIT = new Program(
            "IT-E6",
            ProgramType.CREDIT_BASED,
            required,
            elective,
            electiveCreditRequirement,
            totalCreditRequirement
        );

        return vietNhatIT;
    }

    public static Program generateChemicalEngineeringProgram() {
        List<Course> all_course = createCoursesOnly();
        List<Course> required = new ArrayList<>();

        // --- Các môn đại cương ---
        required.add(findCourseById(all_course, "MI1114"));  // Giải tích I
        required.add(findCourseById(all_course, "MI1124"));  // Giải tích II
        required.add(findCourseById(all_course, "MI1134"));  // Giải tích III
        required.add(findCourseById(all_course, "MI1144"));  // Đại số tuyến tính
        required.add(findCourseById(all_course, "MI2021"));  // Xác suất thống kê
        required.add(findCourseById(all_course, "EM1170"));  // Pháp luật đại cương
        required.add(findCourseById(all_course, "SSH1111")); // Triết học Mác Lênin
        required.add(findCourseById(all_course, "SSH1121")); // Kinh tế chính trị Mác Lênin
        required.add(findCourseById(all_course, "SSH1131")); // Chủ nghĩa xã hội khoa học
        required.add(findCourseById(all_course, "SSH1141")); // Lịch sử ĐCSVN
        required.add(findCourseById(all_course, "SSH1151")); // Tư tưởng Hồ Chí Minh

        // --- Các môn bắt buộc ngành Kỹ thuật hóa học (CH1) ---
        required.add(findCourseById(all_course, "CH2000")); // Nhập môn Kỹ thuật Hóa học
        required.add(findCourseById(all_course, "CH3120")); // Hóa vô cơ
        required.add(findCourseById(all_course, "CH3130")); // Thí nghiệm Hóa vô cơ
        required.add(findCourseById(all_course, "CH3220")); // Hóa hữu cơ
        required.add(findCourseById(all_course, "CH3230")); // Thí nghiệm Hóa hữu cơ
        required.add(findCourseById(all_course, "CH3051")); // Hóa lý I
        required.add(findCourseById(all_course, "CH3052")); // Thí nghiệm Hóa lý I
        required.add(findCourseById(all_course, "CH3061")); // Hóa lý II
        required.add(findCourseById(all_course, "CH3062")); // Thí nghiệm Hóa lý II
        required.add(findCourseById(all_course, "CH3330")); // Hóa phân tích
        required.add(findCourseById(all_course, "CH3340")); // Thí nghiệm Hóa phân tích
        required.add(findCourseById(all_course, "CH3323")); // PP phân tích bằng công cụ
        required.add(findCourseById(all_course, "CH3324")); // TH phân tích bằng công cụ
        required.add(findCourseById(all_course, "CH3400")); // QTTB CN Hóa học 1
        required.add(findCourseById(all_course, "CH3412")); // QTTB CN Hóa học 2
        required.add(findCourseById(all_course, "CH3420")); // QTTB CN Hóa học 3
        required.add(findCourseById(all_course, "CH3480")); // Thí nghiệm QTTB I
        required.add(findCourseById(all_course, "CH3490")); // Thí nghiệm QTTB II
        required.add(findCourseById(all_course, "CH3900")); // Đồ án QTTB
        required.add(findCourseById(all_course, "EE2090")); // Kỹ thuật Điện và ĐK quá trình
        required.add(findCourseById(all_course, "CH3452")); // Mô phỏng trong CN Hóa học
        required.add(findCourseById(all_course, "CH3700")); // Cơ khí ứng dụng
        required.add(findCourseById(all_course, "CH3800")); // Xây dựng công nghiệp

        // required.removeIf(course -> course == null); // Consider adding null checks

        List<Course> elective = new ArrayList<>();

        // --- Các môn học phần tự chọn: Thể dục ---
        elective.add(findCourseById(all_course, "PE1015")); // Thể dục tay không
        elective.add(findCourseById(all_course, "PE1024")); // Bơi lội
        elective.add(findCourseById(all_course, "PE2101")); // Bóng chuyền
        elective.add(findCourseById(all_course, "PE2151")); // Erobic
        elective.add(findCourseById(all_course, "PE2201")); // Bóng đá
        elective.add(findCourseById(all_course, "PE2251")); // Taekwondo
        elective.add(findCourseById(all_course, "PE2261")); // Karatedo
        elective.add(findCourseById(all_course, "PE2301")); // Bóng rổ
        elective.add(findCourseById(all_course, "PE2401")); // Bóng bàn
        elective.add(findCourseById(all_course, "PE2501")); // Cầu lông
        elective.add(findCourseById(all_course, "PE2601")); // Chạy
        elective.add(findCourseById(all_course, "PE2701")); // Nhảy cao
        elective.add(findCourseById(all_course, "PE2801")); // Nhảy xa

        // elective.removeIf(course -> course == null); // Consider adding null checks

        int sumRequiredCredits = 0;
        for (Course c : required) {
            if (c != null) { // Check for null
                sumRequiredCredits += c.getCreditCount();
            }
        }

        int electiveCreditRequirement = 12;
        int totalCreditRequirement = sumRequiredCredits + electiveCreditRequirement;

        Program chemicalEngineering = new Program(
            "CH1",
            ProgramType.CREDIT_BASED,
            required,
            elective,
            electiveCreditRequirement,
            totalCreditRequirement
        );

        return chemicalEngineering;
    }

    public static Program generateEngineeringPhysicsProgram() {
        List<Course> all_course = createCoursesOnly();
        List<Course> required = new ArrayList<>();

        // --- Các môn đại cương ---
        required.add(findCourseById(all_course, "MI1114"));  // Giải tích I
        required.add(findCourseById(all_course, "MI1124"));  // Giải tích II
        required.add(findCourseById(all_course, "MI1134"));  // Giải tích III
        required.add(findCourseById(all_course, "MI1144"));  // Đại số tuyến tính
        required.add(findCourseById(all_course, "MI2021"));  // Xác suất thống kê
        required.add(findCourseById(all_course, "EM1170"));  // Pháp luật đại cương
        required.add(findCourseById(all_course, "SSH1111")); // Triết học Mác Lênin
        required.add(findCourseById(all_course, "SSH1121")); // Kinh tế chính trị Mác Lênin
        required.add(findCourseById(all_course, "SSH1131")); // Chủ nghĩa xã hội khoa học
        required.add(findCourseById(all_course, "SSH1141")); // Lịch sử ĐCSVN
        required.add(findCourseById(all_course, "SSH1151")); // Tư tưởng Hồ Chí Minh

        // --- Các môn bắt buộc ngành Vật lý kỹ thuật (PH1) ---
        required.add(findCourseById(all_course, "PH2010")); // Nhập môn Vật lý kỹ thuật
        required.add(findCourseById(all_course, "EE2012")); // Kỹ thuật điện (Cần đảm bảo EE2012 trong createCoursesOnly phù hợp, hoặc dùng ID riêng như EE2012PH nếu khác)
        required.add(findCourseById(all_course, "ME2115")); // Vẽ kỹ thuật trên máy tính
        required.add(findCourseById(all_course, "PH3010")); // PP toán cho vật lý
        required.add(findCourseById(all_course, "ET2010")); // Kỹ thuật điện tử
        required.add(findCourseById(all_course, "PH2021")); // Đồ án môn học I
        required.add(findCourseById(all_course, "PH3350")); // Căn bản KH máy tính cho kỹ sư vật lý
        required.add(findCourseById(all_course, "PH3060")); // Cơ học lượng tử
        required.add(findCourseById(all_course, "PH3030")); // Trường điện từ
        required.add(findCourseById(all_course, "PH3400")); // Cơ sở quang học, quang điện tử
        required.add(findCourseById(all_course, "PH3110")); // Vật lý chất rắn
        required.add(findCourseById(all_course, "PH3120")); // Vật lý thống kê
        required.add(findCourseById(all_course, "PH3360")); // Tính toán trong vật lý và KH vật liệu
        required.add(findCourseById(all_course, "PH3071")); // Vật lý và kỹ thuật chân không
        required.add(findCourseById(all_course, "PH2022")); // Đồ án môn học II
        required.add(findCourseById(all_course, "PH3190")); // Vật lý và linh kiện bán dẫn
        required.add(findCourseById(all_course, "PH3410")); // Hệ thống nhúng và ứng dụng
        required.add(findCourseById(all_course, "PH3330")); // Vật lý điện tử
        required.add(findCourseById(all_course, "PH4060")); // Công nghệ vật liệu
        required.add(findCourseById(all_course, "PH4490")); // Kỹ thuật xử lý ảnh và ứng dụng
        required.add(findCourseById(all_course, "PH3090")); // Quang học kỹ thuật
        required.add(findCourseById(all_course, "PH4600")); // Cơ sở kỹ thuật ánh sáng

        // required.removeIf(course -> course == null); // Consider adding null checks

        List<Course> elective = new ArrayList<>();

        // --- Các môn học phần tự chọn: Thể dục ---
        elective.add(findCourseById(all_course, "PE1015")); // Thể dục tay không
        elective.add(findCourseById(all_course, "PE1024")); // Bơi lội
        elective.add(findCourseById(all_course, "PE2101")); // Bóng chuyền
        elective.add(findCourseById(all_course, "PE2151")); // Erobic
        elective.add(findCourseById(all_course, "PE2201")); // Bóng đá
        elective.add(findCourseById(all_course, "PE2251")); // Taekwondo
        elective.add(findCourseById(all_course, "PE2261")); // Karatedo
        elective.add(findCourseById(all_course, "PE2301")); // Bóng rổ
        elective.add(findCourseById(all_course, "PE2401")); // Bóng bàn
        elective.add(findCourseById(all_course, "PE2501")); // Cầu lông
        elective.add(findCourseById(all_course, "PE2601")); // Chạy
        elective.add(findCourseById(all_course, "PE2701")); // Nhảy cao
        elective.add(findCourseById(all_course, "PE2801")); // Nhảy xa

        // elective.removeIf(course -> course == null); // Consider adding null checks

        int sumRequiredCredits = 0;
        for (Course c : required) {
            if (c != null) { // Check for null
                sumRequiredCredits += c.getCreditCount();
            }
        }

        int electiveCreditRequirement = 8;
        int totalCreditRequirement = sumRequiredCredits + electiveCreditRequirement;

        Program physicsEngineering = new Program(
            "PH1",
            ProgramType.CREDIT_BASED,
            required,
            elective,
            electiveCreditRequirement,
            totalCreditRequirement
        );

        return physicsEngineering;
    }

    // You can add other generate...Program methods here if needed
    // e.g., generateBioEngineeringProgram, generateBusinessAdminProgram
}