<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>
<h2 class="mb-4">Payment History</h2>

<div class="card shadow p-3 bg-white">
    <div class="table-responsive">
        <table class="table table-bordered align-middle">
            <thead class="table-primary">
                <tr>
                    <th>#</th>
                    <th>Payment ID</th>
                    <th>Booking ID</th>
                    <th>Package</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
              
                <tr>
                    <td>1</td>
                    <td>201</td>
                    <td>101</td>
                    <td>Paris Delight</td>
                    <td>$1200</td>
                    <td><span class="badge bg-success">Successful</span></td>
                    <td>2025-09-24</td>
                </tr>
                
            </tbody>
        </table>
    </div>
</div>

<%@ include file="footer.jsp" %>
