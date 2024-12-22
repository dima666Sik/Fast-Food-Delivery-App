import { Link } from "react-router-dom";
import "./Completion.css";

function Completion(props) {
	return (
		<>
			<section className="completion">
				<h1 className="completion-h1">Thank you for purchase! 🎉</h1>
				<button className="completion-btn">
					<Link to="/foods">Return to choose more purshace?</Link>
				</button>
			</section>
		</>
	);
}

export default Completion;
