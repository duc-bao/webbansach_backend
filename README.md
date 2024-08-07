# Webbansach - Hệ Thống Bán Sách Trực Tuyến

**Webbansach** là một ứng dụng web bán sách trực tuyến được xây dựng với Spring Boot và Elasticsearch, cung cấp một nền tảng mạnh mẽ để quản lý và tìm kiếm sách. Hệ thống hỗ trợ các tính năng như tìm kiếm sách, quản lý đơn hàng, và đánh giá sách, giúp người dùng có trải nghiệm mua sắm sách tiện lợi và hiệu quả.

## Tính Năng Nổi bật
- **Tìm kiếm sách**: Hỗ trợ tìm kiếm sách theo tên, tác giả, và các thuộc tính khác với tính năng fuzzy search và phân tích từ tiếng Việt.
- **Quản lý sách**: Cho phép thêm, sửa, xóa sách trong hệ thống với các thông tin chi tiết như tên sách, tác giả, giá bán, mô tả, và thể loại.
- **Quản lý đơn hàng**: Hỗ trợ tạo và quản lý đơn hàng mua sách, bao gồm việc theo dõi trạng thái đơn hàng và xử lý thanh toán.
- **Đánh giá sách**: Người dùng có thể đánh giá và để lại nhận xét về sách để giúp những người dùng khác đưa ra quyết định mua sắm.
- **Tìm kiếm nâng cao**: Sử dụng Elasticsearch để cung cấp các chức năng tìm kiếm mạnh mẽ với các bộ lọc và sắp xếp tùy chỉnh.
- **Quản lí token**: Sử dụng Redis để quản lí blaclist Token
- **Quản lí giỏ hàng**: Sử dụng Redis để demo giỏ hàng vào giờ cao điểm và thực hiện thao tác đồng bộ dữ liệu từ database và redis(đang thao tác thủ công nên đang được update)
- **Quản lí thanh toán**: Sử dụng VNPAY để thực hiện giao dịch thanh toán
- **GỬi email khi đăng kí tài khoản**: Sẽ cấu hình và sử dụng EmailService để thực hiện gửi email(cái này đang được update bằng cách khác)
## Cài Đặt và Chạy Ứng Dụng
### Prerequisites

- JDK 17 hoặc cao hơn
- Docker và Docker Compose
- Elasticsearch 8.x
  ### Cài Đặt Ứng Dụng

1. **Clone repository**

   ```bash
   git clone [https://github.com/username/webbansach.git](https://github.com/duc-bao/webbansach_backend)
   cd webbansach
2. **Chạy docker compose**: docker-compose up
3. **Thay đổi các thông số về database và cấu hình gửi email trong file application.propertites**
3. **Run dự án bằng Intellij**
# Hy vọng file README này giúp bạn giới thiệu dự án `web ban sach` của mình một cách rõ ràng và đầy đủ!







## Cách thức thực hiện Elasticsearch với việc tìm kiếm bằng Tiếng Việt

Bước 1.**Cài đặt pluggin**
 Cài đặt plugin ICU (nếu chưa có): *bin/elasticsearch-plugin install analysis-icu*
Bước 2: **Tạo file cấu hình cho chỉ mục Index(BOOK)** :
 ![image](https://github.com/user-attachments/assets/33709265-cb32-467e-94d9-fa9f4615e774)
 #### Giải thích:
 Chúng ta sẽ thực hiện điều này cấu hình cho chỉ mục Index VN_Analyzer với các filter là  "filter": ["lowercase", "asciifolding","vi_stop"]
 Và trong code Spring boot thì ta sẽ thực hiện điều này ![image](https://github.com/user-attachments/assets/d0a668b2-0ae1-412d-9200-cde8bb80c303)
 Bước 3:**Run và test cho ra kết quả như hình**:
 ![image](https://github.com/user-attachments/assets/37e242c6-c8bb-4de9-a6fa-4f4f546a0bd3)


  








