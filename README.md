# bankingApp
<hr>
<b>PostMan Requests<br></b>
http://localhost:8080/api/v1/auth/signin<br>
<b>Body:<br></b>
{
"email": "user@gmail.com",
"password": "user"
}

<b>Refresh Token<br></b>
http://localhost:8080/api/v1/auth/refresh
{
"token" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsImlhdCI6MTcwOTE0MDM5OCwiZXhwIjoxNzA5NzQ1MTk4fQ.w_sfP3YJ3eYeRE7IB60LBuoLRa0EIPfkwxsvf4M7XM0"
}

<b>Access To User page:</b><br>
http://localhost:8080/api/v1/user
Bearer Token use given Token from Refresh Token so


