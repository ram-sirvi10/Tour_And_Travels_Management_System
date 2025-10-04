<style>/* ===================== Reset & Body ===================== */
/* ===================== Reset & Body ===================== */
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
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
	transition: background 0.3s ease;
}

nav:hover {
	background: rgba(0, 0, 0, 0.75);
}

nav .logo {
	font-size: 1.7rem;
	font-weight: bold;
	color: #fff;
	letter-spacing: 1px;
}

nav ul {
	list-style: none;
	display: flex;
	gap: 20px;
	flex-wrap: wrap;
}

nav ul li a {
	text-decoration: none;
	color: #fff;
	padding: 8px 16px;
	border-radius: 8px;
	transition: 0.3s;
	white-space: nowrap;
	font-weight: 500;
}

nav ul li a:hover {
	background: #ff6f61;
	color: #fff;
}

/* ===================== Responsive Navbar ===================== */
@media ( max-width : 992px) {
	nav {
		padding: 15px 5%;
	}
}

@media ( max-width : 768px) {
	nav {
		flex-direction: column;
		align-items: flex-start;
		padding: 15px 5%;
	}
	nav ul {
		flex-direction: column;
		width: 100%;
		gap: 10px;
		margin-top: 10px;
	}
	nav ul li a {
		width: 100%;
		text-align: left;
	}
	nav .logo {
		margin-bottom: 10px;
	}
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
/* logo styles (for SVG option) */
.logo {
	text-decoration: none;
	display: inline-flex;
	align-items: center;
	gap: 10px;
	color: #fff;
}

.logo-icon {
	display: block;
	width: 44px;
	height: 44px;
	flex-shrink: 0;
	/* subtle shadow */
	filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.25));
}

.logo-text {
	font-size: 1.1rem;
	color: #fff;
	line-height: 1;
	display: inline-block;
	transform: translateY(-1px);
}

.logo-text strong {
	font-weight: 700;
	letter-spacing: 0.4px;
}

.logo-text .accent {
	color: #ffd6cf;
	margin-left: 2px;
	font-weight: 600;
}

/* hover effect */
nav .logo:hover .logo-icon {
	transform: translateY(-3px) scale(1.03);
	transition: transform .25s ease;
}

nav .logo:hover .logo-text {
	text-decoration: none;
	opacity: 0.95;
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


	<a href="<%=request.getContextPath()%>/"
		class="logo d-flex align-items-center gap-2"
		aria-label="TravelMate - Home"> <!-- SVG icon --> <svg
			class="logo-icon" xmlns="http://www.w3.org/2000/svg"
			viewBox="0 0 64 64" width="36" height="36" aria-hidden="true">
    <defs>
      <linearGradient id="g1" x1="0" x2="1">
        <stop offset="0" stop-color="#ff6f61" />
        <stop offset="1" stop-color="#ff9a8b" />
      </linearGradient>
    </defs>
    <!-- location pin -->
    <path fill="url(#g1)"
				d="M32 4C22 4 14 12 14 22c0 12 14 26 16 28 2-2 16-16 16-28 0-10-8-18-14-18z" />
    <circle cx="32" cy="22" r="6" fill="#fff" />
    <!-- airplane silhouette (white) -->
    <path fill="#fff"
				d="M45 30c0 .6-.3 1.1-.8 1.4L40 34l1 4-5-3-5 3 1-4-4.2-2.6A1.6 1.6 0 0 1 27 30V28c0-.9.8-1.6 1.6-1.6L35 28l6-3-2 5 4 0c.6 0 1.1.3 1.4.8.2.5.2 1.1 0 1.4z" />
  </svg> <!-- text --> <span class="logo-text"> <strong>Travel</strong><span
			class="accent">Mate</span>
	</span>
	</a>

	<ul>
		<li><a href="<%=request.getContextPath()%>/">Home</a></li>
		<li><a href="<%=request.getContextPath()%>/#features">Features</a></li>
		<li><a href="<%=request.getContextPath()%>/#about">About</a></li>
		<li><a href="<%=request.getContextPath()%>/#contact">Contact</a></li>


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