<style>/* ===================== Reset & Body ===================== */
* {
	box-sizing: border-box;
	margin: 0;
	padding: 0;
}

body {
	font-family: 'Segoe UI', sans-serif;
	background: #f9f9f9;
	scroll-behavior: smooth;
	line-height: 1.5;
}

/* ===================== Navbar ===================== */
nav {
	position: fixed;
	top: 0;
	width: 100%;
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 15px 10%;
	background: rgba(0, 0, 0, 0.6);
	backdrop-filter: blur(6px);
	z-index: 1000;
	flex-wrap: wrap;
}

nav .logo {
	font-size: 1.5rem;
	font-weight: bold;
	color: #fff;
}

nav ul {
	list-style: none;
	display: flex;
	gap: 15px;
	flex-wrap: wrap;
}

nav ul li a {
	text-decoration: none;
	color: #fff;
	padding: 8px 14px;
	border-radius: 6px;
	transition: 0.3s;
	white-space: nowrap;
}

nav ul li a:hover {
	background: #ff6f61;
}

/* ===================== Landing Page Header ===================== */
header {
	background:
		url('https://images.unsplash.com/photo-1507525428034-b723cf961d3e')
		center/cover no-repeat;
	height: 100vh;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	text-align: center;
	color: white;
	position: relative;
}

header::after {
	content: "";
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.4);
}

header h1, header p, header a {
	position: relative;
	z-index: 2;
}

header h1 {
	font-size: 3.5rem;
	margin-bottom: 10px;
}

header p {
	font-size: 1.3rem;
	margin-bottom: 20px;
}

.btn {
	text-decoration: none;
	background: linear-gradient(45deg, #ff6f61, #ff9a8b);
	color: white;
	padding: 14px 28px;
	border-radius: 8px;
	transition: 0.3s;
	display: inline-block;
}

.btn:hover {
	transform: scale(1.05);
}

/* ===================== Features Section ===================== */
section {
	padding: 60px 10%;
}

h2 {
	text-align: center;
	margin-bottom: 30px;
	color: #ff6f61;
}

.features {
	display: flex;
	flex-wrap: wrap;
	gap: 25px;
	justify-content: space-around;
}

.feature-box {
	background: white;
	flex: 1 1 250px;
	padding: 25px;
	border-radius: 12px;
	text-align: center;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
	transition: 0.3s;
}

.feature-box img {
	width: 70px;
	height: 70px;
	margin-bottom: 15px;
}

.feature-box:hover {
	transform: translateY(-10px);
	box-shadow: 0 6px 18px rgba(0, 0, 0, 0.2);
}

/* ===================== CTA Section ===================== */
.cta {
	text-align: center;
	background:
		url('https://images.pexels.com/photos/346885/pexels-photo-346885.jpeg')
		center/cover no-repeat;
	color: white;
	padding: 80px 20px;
	border-radius: 12px;
	position: relative;
	margin: 50px 0;
}

.cta::after {
	content: "";
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.5);
}

.btn-started {
	text-decoration: none;
	background: linear-gradient(90deg, #ff6f61, #ff3b2e);
	color: white;
	padding: 16px 32px;
	margin-bottom: 10px;
	border-radius: 50px;
	font-weight: bold;
	animation: pulse 2s infinite;
	transition: 0.3s;
	position: relative; /* Add this */
	z-index: 2; /* Add this */
}

.btn-started:hover {
	transform: translateY(-5px);
}

@
keyframes pulse { 0%{
	box-shadow: 0 0 0 0 rgba(255, 111, 97, 0.7);
}

70




%
{
box-shadow




:




0




0




0




20px




rgba


(




255
,
111
,
97
,
0




)


;
}
100




%
{
box-shadow




:




0




0




0




0




rgba


(




255
,
111
,
97
,
0




)


;
}
}

/* ===================== Footer ===================== */
.footer {
	background: #333;
	color: white;
	text-align: center;
	padding: 20px;
}

/* ===================== Forms ===================== */
.container {
	background: rgba(255, 255, 255, 0.95);
	padding: 40px;
	border-radius: 12px;
	width: 400px;
	margin: 120px auto;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.container h2 {
	color: #ff6f61;
	text-align: center;
	margin-bottom: 20px;
}

input {
	width: 100%;
	padding: 10px;
	margin: 10px 0;
	border: 1px solid #ccc;
	border-radius: 6px;
}

input:focus {
	border-color: #ff6f61;
	outline: none;
	box-shadow: 0 0 5px rgba(255, 111, 97, 0.5);
}

button, input[type=submit] {
	background: #ff6f61;
	border: none;
	color: white;
	padding: 12px;
	width: 100%;
	border-radius: 6px;
	cursor: pointer;
	font-size: 1rem;
}

button:hover, input[type=submit]:hover {
	background: #e95a4f;
}

.error {
	color: red;
	font-size: 0.9rem;
	margin: 5px 0;
	text-align: left;
}

.success {
	color: green;
	font-size: 0.9rem;
	margin: 5px 0;
	text-align: left;
}

/* ===================== Tabs for Registration ===================== */
.tab-nav {
	text-align: center;
	margin-bottom: 20px;
}

.tab-nav .btn {
	margin: 0 10px;
	display: inline-block;
}

.tab-nav .active {
	background: #ff3b2e;
	color: white;
}

/* ===================== Responsive ===================== */
@media ( max-width :768px) {
	nav ul {
		flex-direction: column;
		gap: 10px;
	}
	header h1 {
		font-size: 2.5rem;
	}
	.features {
		flex-direction: column;
	}
	.container {
		width: 90%;
		margin: 80px auto;
		padding: 20px;
	}
}
</style>
<%
Object obj = session.getAttribute("user");
if (obj == null) {
	obj = session.getAttribute("agency");
}
String label = obj != null ? "My Account" : "Login";
%>
<nav>

	<div class="logo">TravelMate</div>
	<ul>
		<li><a href="index.jsp">Home</a></li>

		<li><a href="login.jsp"> <%=label%>
		</a></li>
		<%
		if (obj == null) {
		%>
		<li><a href="registerUser.jsp">Register</a></li>
		<%
		}
		%>
	</ul>


</nav>
