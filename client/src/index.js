import React from "react";
import ReactDOM from "react-dom/client";
import { Provider } from "react-redux";
import { BrowserRouter as Router } from "react-router-dom";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import App from "./components/app/App";
import store from "./redux/store/store";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
	<Router>
		<Provider store={store}>
			<App />
		</Provider>
	</Router>
);
