import "./App.css";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";

import Routers from "../../routes/Routers";
import Footer from "../footer/Footer";
import Header from "../header/Header";
import { setUser } from "../../redux/store/user/userSlice";

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
