import React, { useEffect, useState } from "react";
import { Tab, Tabs } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import "./Profile.css"; // імпортуємо CSS-файл для стилізації
import CartTable from "../../components/ui/cart-table/CartTable";
import axios from "axios";
import { axiosLogout, setUser } from "../../redux/store/user/userSlice";
import { refresh } from "../../actions/post/refresh";

const Profile = () => {
	const dispatch = useDispatch();
	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);
	const navigate = useNavigate();

	const accessToken = useSelector((state) => state.user.accessToken);

	const userInfo = useSelector((state) => state.user);
	const [orders, setOrders] = useState([]);

	useEffect(() => {
		const fetchData = async () => {
			if (accessToken) {
				axios
					.get(
						`${process.env.REACT_APP_SERVER_API_URL}api/v1/private/order-purchase/get-order-with-purchase-user`,
						{
							headers: {
								Authorization: "Bearer " + accessToken,
							},
						}
					)
					.then((response) => {
						const data = response.data;
						const modifiedData = data.map((order) => {
							const modifiedItems = order.purchaseItems.map((item) => {
								const modifiedImage = `${process.env.REACT_APP_SERVER_API_URL}public/images/products/${item.image01}`;
								return { ...item, image01: modifiedImage };
							});
							return { ...order, purchaseItems: modifiedItems };
						});
						setOrders(modifiedData);
						console.log(modifiedData);
					})
					.catch(async (error) => {
						console.log(error.response, accessToken);
						if (
							error.response.data ===
							"Access token was expired! Refresh is valid!"
						) {
							try {
								const response = await refresh(accessToken);

								if (response.status === 200) {
									if (response.data.access_token) {
										// Dispatch the setUser action
										dispatch(
											setUser({
												accessToken: response.data.access_token,
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
		if (!isAuthenticated) {
			navigate("/");
		}
	}, [accessToken, isAuthenticated, navigate]);

	return (
		<div className="profile-container">
			<Tabs
				defaultActiveKey="order"
				id="uncontrolled-tab-example"
				className="mb-3"
			>
				<Tab eventKey="order" title="Orders">
					<div className="tab-content">
						{orders.length === 0 ? (
							<h5 className="text-center">You have no orders</h5>
						) : (
							<div>
								{orders.map((order) => (
									<div key={order.id} className="order-details">
										<h6 className="order-id">Order ID: {order.id}</h6>
										<p className="order-info">Phone: {order.phone}</p>
										<p className="order-info">
											Order Date: {order.order_date_arrived}
										</p>
										<p className="order-info">
											Order Time: {order.order_time_arrived}
										</p>
										<p className="order-info">
											Total Amount: {order.total_amount}
										</p>
										<p className="order-info">
											Delivery: {order.delivery ? "Yes" : "No"}
										</p>
										{order.delivery && (
											<div className="delivery-details">
												<p>City: {order.city}</p>
												<p>Street: {order.street}</p>
												<p>House Number: {order.house_number}</p>
												<p>Flat Number: {order.flat_number}</p>
												<p>Floor Number: {order.floor_number}</p>
											</div>
										)}
										<h6>Purchase Items:</h6>
										<CartTable
											cartItems={order.purchaseItems}
											showDelete={false}
										/>
									</div>
								))}
							</div>
						)}
					</div>
				</Tab>
				<Tab eventKey="profile" title="Profile">
					<div className="tab-content">
						<h5 className="text-center">Profile Details</h5>
						<p>First Name: {userInfo.firstName}</p>
						<p>Last Name: {userInfo.lastName}</p>
						<p>Email: {userInfo.email}</p>
						{/* Додаткові деталі про профіль користувача */}
					</div>
				</Tab>

				<Tab eventKey="contact" title="Contact" disabled>
					<div className="tab-content">Tab content for Contact</div>{" "}
					{/* Додаємо стилізований контент */}
				</Tab>
			</Tabs>
		</div>
	);
};

export default Profile;
