import "./Dashboard.css";
import { Link } from "react-router-dom";

const Dashboard = () => {
	return (
		<div className="dashboard-container">
			<div>
				<ul className="dashboard-list">
					<li className="dashboard-item">
						<Link to="/students" className="dashboard-link">
							<button className="dashboard-button">
								View Students by Event
							</button>
						</Link>
					</li>
					<li className="dashboard-item">
						<Link to="/events" className="dashboard-link">
							<button className="dashboard-button">
								Events
							</button>
						</Link>
					</li>
					<li className="dashboard-item">
						<Link to="/alumni" className="dashboard-link">
							<button className="dashboard-button">View Alumni data</button>
						</Link>
					</li>
				</ul>
			</div>
		</div>
	);
};

export default Dashboard;
