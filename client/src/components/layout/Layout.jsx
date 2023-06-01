import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";

import Routers from "../../routes/Routers";
import Footer from "../footer/Footer";
import Header from "../header/Header";
import {
	axiosLogout,
	clearUser,
	setInfoUser,
	setUser,
} from "../../redux/store/user/userSlice";
import { cartActions } from "../../redux/store/shopping-cart/cartSlice";
import { cartActionsLiked } from "../../redux/store/shopping-cart/cartsLikedSlice";
import { refresh } from "../../actions/post/refresh";

const Layout = () => {
	const dispatch = useDispatch();

	const accessToken = useSelector((state) => state.user.accessToken);

	useEffect(() => {
		const fetchData = async () => {
			if (accessToken) {
				axios
					.get(
						`${process.env.REACT_APP_SERVER_API_URL}api/v1/user/get-access-data`,
						{
							headers: {
								Authorization: "Bearer " + accessToken,
							},
						}
					)
					.then((response) => {
						dispatch(
							setInfoUser({
								firstName: response.data.first_name,
								lastName: response.data.last_name,
								email: response.data.email,
							})
						);
					})
					.catch(async (error) => {
						console.log(error.response, accessToken);
						if (
							error.response.data ===
							"Access token was expired! Refresh is valid!"
						) {
							try {
								const responseToken = await refresh(accessToken);

								if (responseToken.status === 200) {
									console.log(responseToken);
									if (responseToken.data.access_token) {
										// Dispatch the setUser action
										dispatch(
											setUser({
												accessToken: responseToken.data.access_token,
											})
										);
									}
								}
							} catch (error) {
								console.log(error);
								throw error; // пробрасываем ошибку выше для обработки в setLikes
							}
						}

						if (
							error.response.data ===
								"You need to reauthorize! Tokens all were expired. You will be much to authorization!" ||
							error.response.data ===
								"All Tokens (access & refresh) were expired! Please generate news tokens!" ||
							error.response.data === "Valid Refresh token was expired..." ||
							error.response.data === "Tokens from client is bad!"
						) {
							dispatch(
								axiosLogout({
									accessToken,
								})
							);
						}
					});
			}
		};
		fetchData();
	}, [accessToken]);

	return (
		<div>
			<Header />
			<Routers />
			<Footer />
		</div>
	);
};

export default Layout;
