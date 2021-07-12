<script type="text/javascript">

let username = 'huidong'
let password = '200176'
let role = 'USER'
let books = [
  {
    id: 1,
    bookname: '',
    author: '',
    type: '',
    isbn: '',
    price: 12,
    introduction: '',
    image: '',
    inventory: 2,
    state: 1
  }
]
let users = [
  {
    id: 1,
    username: '',
    password: '',
    email: '',
    role: '',
    state: ''
  }
]
let carts = [
  {
    id: 1,
    cartid: 1,
    bookid: 1,
    bookcount: 1
  }
]
let orders = [
  {
    id: 1,
    userid: 1,
    timestamp: 1,
    price: 0,
    receivername: ' ',
    address: ' '
  }
]
let orderitems = [
  {
    id: 1,
    orderid: 1,
    bookid: 1,
    bookcount: 1,
    bookprice: 1
  }
]
function hello () {
  console.log('hello world')
}
export default
{
  username,
  password,
  books,
  users,
  hello,
  carts,
  role,
  orders,
  orderitems
}
</script>
