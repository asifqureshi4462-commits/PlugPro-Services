<?php
require_once __DIR__ . '/config.php';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PlugPro - Administrator Portal</title>
    <style>
        :root {
            --primary: #F59E0B;
            --primary-dark: #D97706;
            --bg: #F8FAFC;
            --surface: #FFFFFF;
            --text-dark: #0F172A;
            --text-muted: #64748B;
            --border: #E2E8F0;
            --success: #059669;
            --danger: #DC2626;
            --warning: #D97706;
        }
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; }
        body { background: var(--bg); color: var(--text-dark); display: flex; min-height: 100vh; }
        
        /* Sidebar */
        .sidebar { width: 260px; background: #0F172A; color: #FFF; padding: 24px 16px; flex-shrink: 0; }
        .logo-row { display: flex; align-items: center; gap: 12px; margin-bottom: 32px; padding: 0 8px; }
        .logo-row h2 { font-size: 20px; font-weight: 700; color: #FFF; }
        .badge { background: var(--primary); color: #FFF; padding: 2px 8px; border-radius: 6px; font-size: 11px; }
        .nav-item { display: flex; align-items: center; gap: 12px; padding: 12px 14px; color: #94A3B8; text-decoration: none; border-radius: 8px; margin-bottom: 4px; font-size: 14px; font-weight: 500; cursor: pointer; }
        .nav-item:hover, .nav-item.active { background: #1E293B; color: #FFF; }
        
        /* Main Container */
        .main-content { flex: 1; padding: 32px; overflow-y: auto; }
        .top-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 28px; }
        .top-header h1 { font-size: 24px; font-weight: 700; }
        
        /* Metric Cards */
        .metrics-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; margin-bottom: 32px; }
        .metric-card { background: var(--surface); padding: 24px; border-radius: 16px; border: 1dp solid var(--border); box-shadow: 0 1px 3px rgba(0,0,0,0.05); }
        .metric-label { font-size: 13px; color: var(--text-muted); font-weight: 500; }
        .metric-value { font-size: 28px; font-weight: 700; margin-top: 8px; color: var(--text-dark); }
        .metric-value.amber { color: var(--primary-dark); }
        
        /* Tables */
        .table-card { background: var(--surface); border-radius: 16px; border: 1px solid var(--border); padding: 20px; margin-bottom: 32px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }
        .table-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
        .table-header h3 { font-size: 17px; font-weight: 600; }
        table { width: 100%; border-collapse: collapse; text-align: left; }
        th { font-size: 12px; font-weight: 600; color: var(--text-muted); text-transform: uppercase; padding: 12px 14px; border-bottom: 1px solid var(--border); }
        td { font-size: 14px; padding: 14px; border-bottom: 1px solid var(--border); }
        tr:last-child td { border-bottom: none; }
        
        /* Badges & Buttons */
        .status-badge { display: inline-block; padding: 4px 10px; border-radius: 12px; font-size: 11px; font-weight: 600; }
        .status-verified { background: #ECFDF5; color: var(--success); }
        .status-pending { background: #FEF3C7; color: var(--warning); }
        .status-rejected { background: #FEF2F2; color: var(--danger); }
        .btn { padding: 8px 14px; border-radius: 8px; border: none; font-size: 13px; font-weight: 500; cursor: pointer; }
        .btn-success { background: var(--success); color: #FFF; }
        .btn-danger { background: var(--danger); color: #FFF; }
        .btn-primary { background: var(--primary); color: #FFF; }
    </style>
</head>
<body>

    <!-- Sidebar -->
    <div class="sidebar">
        <div class="logo-row">
            <h2>PlugPro</h2>
            <span class="badge">Admin</span>
        </div>
        <div class="nav-item active">Dashboard</div>
        <div class="nav-item">Providers &amp; Verification</div>
        <div class="nav-item">Customer Bookings</div>
        <div class="nav-item">Service Catalog</div>
        <div class="nav-item">Users</div>
        <div class="nav-item">Settings</div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="top-header">
            <div>
                <h1>Executive Overview</h1>
                <p style="color: var(--text-muted); font-size: 14px; margin-top: 4px;">Monitor platform performance, verify technicians, and manage bookings.</p>
            </div>
            <button class="btn btn-primary" onclick="alert('PlugPro Service Sync OK')">+ Add New Category</button>
        </div>

        <!-- Metrics -->
        <div class="metrics-grid">
            <div class="metric-card">
                <div class="metric-label">Total Revenue</div>
                <div class="metric-value amber">₹184,500</div>
            </div>
            <div class="metric-card">
                <div class="metric-label">Active Bookings</div>
                <div class="metric-value">48</div>
            </div>
            <div class="metric-card">
                <div class="metric-label">Verified Professionals</div>
                <div class="metric-value">124</div>
            </div>
            <div class="metric-card">
                <div class="metric-label">Pending Verifications</div>
                <div class="metric-value" style="color: var(--warning);">7</div>
            </div>
        </div>

        <!-- Provider Verification Table -->
        <div class="table-card">
            <div class="table-header">
                <h3>Provider Verification Requests</h3>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Technician Name</th>
                        <th>Profession</th>
                        <th>Experience</th>
                        <th>Hourly Rate</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="providerTableBody">
                    <tr>
                        <td><strong>James Carter</strong></td>
                        <td>Master Electrician</td>
                        <td>8 Years</td>
                        <td>₹450/hr</td>
                        <td><span class="status-badge status-verified">Verified</span></td>
                        <td><button class="btn btn-danger" onclick="this.closest('tr').remove()">Deactivate</button></td>
                    </tr>
                    <tr>
                        <td><strong>Sarah Miller</strong></td>
                        <td>Plumbing Specialist</td>
                        <td>6 Years</td>
                        <td>₹380/hr</td>
                        <td><span class="status-badge status-verified">Verified</span></td>
                        <td><button class="btn btn-danger" onclick="this.closest('tr').remove()">Deactivate</button></td>
                    </tr>
                    <tr>
                        <td><strong>Michael Brown</strong></td>
                        <td>AC &amp; Appliance Repair</td>
                        <td>4 Years</td>
                        <td>₹400/hr</td>
                        <td><span class="status-badge status-pending">Pending Approval</span></td>
                        <td>
                            <button class="btn btn-success" onclick="this.closest('tr').querySelector('.status-badge').className='status-badge status-verified'; this.closest('tr').querySelector('.status-badge').innerText='Verified';">Approve</button>
                            <button class="btn btn-danger" onclick="this.closest('tr').querySelector('.status-badge').className='status-badge status-rejected'; this.closest('tr').querySelector('.status-badge').innerText='Rejected';">Reject</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- Recent Bookings Table -->
        <div class="table-card">
            <div class="table-header">
                <h3>Live Customer Bookings</h3>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Booking ID</th>
                        <th>Customer</th>
                        <th>Service</th>
                        <th>Provider</th>
                        <th>Scheduled Date</th>
                        <th>Amount</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>#BK1094</td>
                        <td>Alex Johnson</td>
                        <td>Electrical Repair</td>
                        <td>James Carter</td>
                        <td>25 Sep 2026, 05:00 PM</td>
                        <td>₹499</td>
                        <td><span class="status-badge status-pending">Pending</span></td>
                    </tr>
                    <tr>
                        <td>#BK1093</td>
                        <td>Emily Davis</td>
                        <td>Plumbing Installation</td>
                        <td>Sarah Miller</td>
                        <td>24 Sep 2026, 11:00 AM</td>
                        <td>₹649</td>
                        <td><span class="status-badge status-verified">Completed</span></td>
                    </tr>
                </tbody>
            </table>
        </div>

    </div>

</body>
</html>
