import "./App.css";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";

import Routers from "../../routes/Routers";
import Footer from "../footer/Footer";
import Header from "../header/Header";
import { axiosLogout, setUser } from "../../redux/store/user/userSlice";
import { refresh } from "../../actions/post/refresh";
import { decodeToken } from "react-jwt";

const App = () => {
	const dispatch = useDispatch();
	const accessToken = useSelector((state) => state.user.accessToken);

	useEffect(() => {
		const fetchData = async () => {
			if (accessToken) {
				dispatch(
					setUser({
						accessToken,
					})
				);
			}
		};
		fetchData();
	}, []);

	return (
		<div>
			<Header />
			<Routers />
			<Footer />
		</div>
	);
};

export default App;
