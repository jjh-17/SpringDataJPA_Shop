[기능 목록]
1. 회원 기능
   1. 회원 등록
   2. 회원 조회
   
2. 상품 기능
   1. 상품 등록 - 도서, 음반, 영화
   2. 상품 수정 - 재고 관리 포함
   3. 상품 조회 - 카테고리 별로 조회 가능

3. 주문 기능
   1. 상품 주문 - 배송 정보 입력
   2. 주문 내역 조회
   3. 주문 취소

[엔티티 분석]
1. Member(회원)
   1. id : Long
   2. name : String
   3. address : Address
   4. orders : List

2. Order(주문)
   1. id : Long
   2. member : Member
   3. orderItems : List
   4. orderDate : Date
   5. status : OrderStatus(열거형)
   
3. Delivery(배송)
   1. id : Long
   2. order : Order
   3. address : Address
   4. status : DeliveryStatus(열거형)
   
4. OrderProduct(주문 상품)
   1. id : Long
   2. item : Item
   3. order : Order
   4. orderPrice : Int
   5. count : Int
   
5. Product(상품)
   1. id : Long
   2. name : String
   3. price : Int
   4. stockQuantity: Int
   5. categories : List

6. Category(카테고리)
   1. id : Long
   2. name : String
   3. items : List
   4. parent : Category
   5. child : List

7. Address
   1. city
   2. street
   3. zipcode
   
8. Album
   1. artist
   2. etc

9. Book
   1. author : String
   2. isbn
   
10. Movie 
   1. director : String
   2. actor


































