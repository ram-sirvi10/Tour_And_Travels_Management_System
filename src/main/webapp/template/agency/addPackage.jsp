<%@ include file="header.jsp" %>
<div class="card shadow">
  <div class="card-header bg-primary text-white">
    <h4>Add New Travel Package</h4>
  </div>
  <div class="card-body">
    <form method="post" action="AddPackageServlet" enctype="multipart/form-data">
      <div class="mb-3">
        <label class="form-label">Title</label>
        <input type="text" name="title" class="form-control" required>
      </div>
      <div class="mb-3">
        <label class="form-label">Description</label>
        <textarea name="description" class="form-control"></textarea>
      </div>
      <div class="row">
        <div class="col-md-6 mb-3">
          <label class="form-label">Location</label>
          <input type="text" name="location" class="form-control" required>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Price (₹)</label>
          <input type="number" name="price" class="form-control" required>
        </div>
      </div>
      <div class="row">
        <div class="col-md-4 mb-3">
          <label class="form-label">Duration (days)</label>
          <input type="number" name="duration" id="duration" class="form-control" onchange="generateDays()" required>
        </div>
        <div class="col-md-4 mb-3">
          <label class="form-label">Total Seats</label>
          <input type="number" name="totalseats" class="form-control" required>
        </div>
        <div class="col-md-4 mb-3">
          <label class="form-label">Departure Date</label>
          <input type="date" name="departure_date" class="form-control" required>
        </div>
      </div>
      <div class="row">
        <div class="col-md-6 mb-3">
          <label class="form-label">Last Booking Date</label>
          <input type="date" name="last_booking_date" class="form-control" required>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Image</label>
          <input type="file" name="imageurl" class="form-control">
        </div>
      </div>
      <div id="days-container"></div>
      <button type="submit" class="btn btn-success mt-3">Save Package</button>
    </form>
  </div>
</div>

<script>
function generateDays(){
  var duration = document.getElementById("duration").value;
  var container = document.getElementById("days-container");
  container.innerHTML = "";
  for(let i=1; i<=duration; i++){
    container.innerHTML += 
      `<div class="mb-3 border p-2 rounded">
         <label class="form-label">Day ${i} Activity</label>
         <input type="text" name="activity${i}" class="form-control mb-2" placeholder="Title of activity">
         <textarea name="description${i}" class="form-control" placeholder="Details for Day ${i}"></textarea>
       </div>`;
  }
}
</script>
  </div>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
