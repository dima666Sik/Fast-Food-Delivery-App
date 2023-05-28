import React from "react";
import ReactDOM from "react-dom/client";

import App from "./components/app/App";

import { BrowserRouter as Router } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import { Provider } from "react-redux";

import store from "./redux/store/store";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
	// <React.StrictMode>
	<Router>
		<Provider store={store}>
			<App />
		</Provider>
	</Router>
	// </React.StrictMode>
);
