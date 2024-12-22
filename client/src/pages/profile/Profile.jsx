import axios from "axios";
import React, { useEffect, useState } from "react";
import { Button, Col, Container, Form, Tab, Tabs } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { refresh } from "../../actions/post/refresh";
import { registerAdmin } from "../../actions/post/registerAdmin";
import AlertText from "../../components/alerts/AlertText";
import ModalAlert from "../../components/alerts/ModalAlert";
import Helmet from "../../components/helmet/Helmet";
import CartTable from "../../components/ui/cart-table/CartTable";
import CommonAd from "../../components/ui/common-ad/CommonAd";
import { useValidationAuthForms } from "../../hooks/useValidationAuthForms";
import { useValidFormsBtn } from "../../hooks/useValidFormsBtn";
import { axiosLogout, setUser } from "../../redux/store/user/userSlice";
import PopularOrderTimeChart from "./analitics/PopularOrderTimeChart";
import ProductPopularityChart from "./analitics/ProductPopularityChart";
import { registerCourier } from "../../actions/post/registerCourier";
import "./Profile.css";

const Profile = () => {
	const dispatch = useDispatch();
	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);
	const navigate = useNavigate();
	const [showTextModal, setShowTextModal] = useState("");
	const [showModal, setShowModal] = useState(false);
	const accessToken = useSelector((state) => state.user.accessToken);

	const userInfo = useSelector((state) => state.user);
	const [orders, setOrders] = useState([]);

	const [title, setTitle] = useState("");
	const [category, setCategory] = useState("Burger");
	const [description, setDescription] = useState("");
	const [price, setPrice] = useState("");
	const [img01, setImg01] = useState(null);
	const [img02, setImg02] = useState(null);
	const [img03, setImg03] = useState(null);

	const {
		email,
		setEmail,
		password,
		setPassword,
		emailDirty,
		setEmailDirty,
		passwordDirty,
		setPasswordDirty,
		emailError,
		passwordError,
		emailHandler,
		passwordHandler,
		confirmPassword,
		confirmPasswordDirty,
		confirmPasswordError,
		confirmPasswordHandler,
		setConfirmPasswordDirty,
		setConfirmPassword,
		firstName,
		setFirstName,
		lastName,
		setLastName,
	} = useValidationAuthForms();

	const { formValid, setFormValid } = useValidFormsBtn();
	useEffect(() => {
		setFormValid(
			!(!email || !password || !firstName || !lastName) &&
				!(emailError || passwordError || confirmPasswordError)
		);
	}, [
		emailError,
		passwordError,
		firstName,
		lastName,
		email,
		password,
		confirmPasswordError,
	]);
	useEffect(() => {
		window.scrollTo(0, 0);
	}, []);

	useEffect(() => {
		const fetchData = async () => {
			if (accessToken) {
				axios
					.get(
						`${process.env.REACT_APP_SERVER_API_URL}api/v2/order-purchase/private/get-orders-with-purchase-user`,
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
						console.log(error, accessToken);
						if (
							error.response.data.message === "Access token has expired" ||
							error.response.data.message === "Access token has revoked"
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
							error.response.data.message ===
								"Access & Refresh tokens have expired" ||
							error.response.data.message === "Access token not found" ||
							error.response.data.message === "Refresh token not found" ||
							error.response.data.message === "Invalid token" ||
							error.response.data.message.startsWith("JWT expired at")
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

	const handleSubmit = async (event) => {
		event.preventDefault();
		console.log(event);
		console.log(title, " ", category);
		console.log(img01);

		// Створення об'єкту з даними для відправки на сервер
		const formData = new FormData();
		formData.append("files", img01);
		formData.append("files", img02);
		formData.append("files", img03);

		const data = JSON.stringify({
			title,
			category,
			description,
			price,
		});

		// Добавляем объект данных как JSON
		formData.append("data", data);

		console.log(Object.fromEntries(formData.entries()));
		// Відправка даних на сервер
		try {
			const response = await axios.post(
				`${process.env.REACT_APP_SERVER_API_URL}api/v2/product/admin-foods/add-product`,
				formData,
				{
					headers: {
						Authorization: "Bearer " + accessToken,
					},
				}
			);

			if (response.status === 200) {
				console.log("yey");
			}
		} catch (error) {
			console.log(error.response.status);
			if (error.response.status === 500) {
				setShowTextModal("Product added successfull!");
				setShowModal(true);
			}
			console.log(error, accessToken);
			if (
				error.response.data.message === "Access token has expired" ||
				error.response.data.message === "Access token has revoked"
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
						setShowTextModal(response.data.message);
						setShowModal(true);
					}
				} catch (error) {
					console.log(error);
					throw error;
				}
			}

			if (
				error.response.data.message ===
					"Access & Refresh tokens have expired" ||
				error.response.data.message === "Access token not found" ||
				error.response.data.message === "Refresh token not found" ||
				error.response.data.message === "Invalid token" ||
				error.response.data.message.startsWith("JWT expired at")
			) {
				dispatch(
					axiosLogout({
						accessToken,
					})
				);
			}
		}
	};

	const handleRegisterAdminClick = async () => {
		const userData = {
			first_name: firstName,
			last_name: lastName,
			email: email,
			password: password,
		};

		try {
			const response = await registerAdmin(userData);

			if (response.status === 201) {
				console.log(userData);
				console.log("Register in:", response.data);
				setShowTextModal("Register Admin was successful!");
				setShowModal(true);
			}
		} catch (error) {
			console.error(error);
			setShowTextModal(error.response.data.message);
			setShowModal(true);
		}
	};

	const handleRegisterCourierClick = async () => {
		const userData = {
			first_name: firstName,
			last_name: lastName,
			email: email,
			password: password,
		};

		try {
			const response = await registerCourier(userData);

			if (response.status === 201) {
				console.log(userData);
				console.log("Register in:", response.data);
				setShowTextModal("Register Courier was successful!");
				setShowModal(true);
			}
		} catch (error) {
			console.error(error);
			setShowTextModal(error.response.data.message);
			setShowModal(true);
		}
	};
	const [img01Preview, setImg01Preview] = useState(null);
	const [img02Preview, setImg02Preview] = useState(null);
	const [img03Preview, setImg03Preview] = useState(null);

	const previewPhoto = (event, setImg, setImgPreview) => {
		const file = event.target.files[0];
		setImg(file);

		const reader = new FileReader();
		reader.onload = () => {
			setImgPreview(reader.result);
		};
		reader.readAsDataURL(file);
	};

	const [data, setData] = useState([]);
	const [ordersChart, setOrdersChart] = useState([]);
	// const [selectedOrdersChartTrans, setSelectedOrdersChartTrans] = useState([]);

	const [selectedOrders, setSelectedOrders] = useState([]);

	const handleCheckboxChange = (orderId) => {
		setSelectedOrders((prev) =>
			prev.includes(orderId)
				? prev.filter((id) => id !== orderId)
				: [...prev, orderId]
		);
	};
	const [isOrderConfirmed, setIsOrderConfirmed] = useState(false);
	// Обработчик нажатия кнопки "Подтвердить"
	const handleConfirmSelection = async () => {
		// const selectedOrderDetails = ordersChart.filter((order) =>
		// 	selectedOrders.includes(order.id)
		// );
		if (isOrderConfirmed) setIsOrderConfirmed(false);
		try {
			const response = await axios.post(
				`${
					process.env.REACT_APP_SERVER_API_URL
				}api/v2/order-purchase/private/order-acceptance?order_ids=${encodeURIComponent(
					selectedOrders.join(",")
				)}&status=IN_TRANSIT`,
				{},
				{
					headers: {
						Authorization: "Bearer " + accessToken,
					},
				}
			);
			if (response.status === 200) {
				console.log(selectedOrders);
				console.log("Выбранные заказы:", response.data);
				setIsOrderConfirmed(true);
			}
		} catch (error) {
			console.log(error, accessToken);
			if (
				error.response.data.message === "Access token has expired" ||
				error.response.data.message === "Access token has revoked"
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
						setShowTextModal(response.data.message);
						setShowModal(true);
					}
				} catch (error) {
					console.log(error);
					throw error;
				}
			}

			if (
				error.response.data.message ===
					"Access & Refresh tokens have expired" ||
				error.response.data.message === "Access token not found" ||
				error.response.data.message === "Refresh token not found" ||
				error.response.data.message === "Invalid token" ||
				error.response.data.message.startsWith("JWT expired at")
			) {
				dispatch(
					axiosLogout({
						accessToken,
					})
				);
			}
		}
	};

	useEffect(() => {
		const fetchDataPopOrderTimeChart = async () => {
			console.log("Viiiiii");
			try {
				const response = await axios.get(
					`${process.env.REACT_APP_SERVER_API_URL}api/v2/order-purchase/get-all-purchases`
				);
				console.log(response.data);
				console.log("Viiiiii2");

				const modifiedData = response.data.map((order) => {
					const modifiedItems = order.purchaseItems.map((item) => {
						const modifiedImage = `${process.env.REACT_APP_SERVER_API_URL}public/images/products/${item.image01}`;
						return { ...item, image01: modifiedImage };
					});
					return { ...order, purchaseItems: modifiedItems };
				});
				console.log(modifiedData);
				console.log("Viiiiii3");

				if (response.status === 200) {
					console.log("Viiiiii4");
					console.log(modifiedData);
					setData(modifiedData);
					setOrdersChart(modifiedData);
				}
			} catch (error) {
				console.error(error);
				setShowTextModal(error.response.data.message);
				setShowModal(true);
			}
		};
		if (userInfo.roles.includes("ADMIN") || userInfo.roles.includes("COURIER"))
			fetchDataPopOrderTimeChart();
	}, [isOrderConfirmed, userInfo.roles]);

	useEffect(() => {
		const fetchDataPopOrderTimeChart = async () => {
			console.log("Viiiiii");
			try {
				const response = await axios.get(
					`${process.env.REACT_APP_SERVER_API_URL}api/v2/order-purchase/private/get-orders-courier-processes`,
					{},
					{
						headers: {
							Authorization: "Bearer " + accessToken,
						},
					}
				);
				console.log(response.data);
				console.log("Viiiiii2");

				const modifiedData = response.data.map((order) => {
					const modifiedItems = order.purchaseItems.map((item) => {
						const modifiedImage = `${process.env.REACT_APP_SERVER_API_URL}public/images/products/${item.image01}`;
						return { ...item, image01: modifiedImage };
					});
					return { ...order, purchaseItems: modifiedItems };
				});
				console.log(modifiedData);
				console.log("Viiiiii3");

				if (response.status === 200) {
					console.log("Viiiiii4");
					console.log(modifiedData);
					setData(modifiedData);
					setOrdersChart(modifiedData);
				}
			} catch (error) {
				console.log(error, accessToken);
				if (
					error.response.data.message === "Access token has expired" ||
					error.response.data.message === "Access token has revoked"
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
							setShowTextModal(response.data.message);
							setShowModal(true);
						}
					} catch (error) {
						console.log(error);
						throw error;
					}
				}

				if (
					error.response.data.message ===
						"Access & Refresh tokens have expired" ||
					error.response.data.message === "Access token not found" ||
					error.response.data.message === "Refresh token not found" ||
					error.response.data.message === "Invalid token" ||
					error.response.data.message.startsWith("JWT expired at")
				) {
					dispatch(
						axiosLogout({
							accessToken,
						})
					);
				}
			}
		};
		if (userInfo.roles.includes("COURIER")) fetchDataPopOrderTimeChart();
	}, [userInfo.roles]);

	return (
		<Helmet title="Profile">
			<CommonAd title={`Your Profile - ${userInfo.firstName}`} />
			<div className="profile-container">
				{userInfo.roles.includes("USER") ? (
					<Tabs
						defaultActiveKey="profile"
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
												<p className="order-info">
													Status: {order.status_order}
												</p>
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
							</div>
						</Tab>

						<Tab eventKey="contact" title="Contact" disabled>
							<div className="tab-content">Tab content for Contact</div>
						</Tab>
					</Tabs>
				) : userInfo.roles.includes("ADMIN") ? (
					<Tabs
						defaultActiveKey="add-product"
						id="uncontrolled-tab-example"
						className="mb-3"
					>
						<Tab eventKey="add-product" title="Add product">
							<div className="tab-content">
								<Container>
									<Form encType="multipart/form-data">
										<Form.Group className="mb-4">
											<Form.Label>Enter title product:</Form.Label>
											<Form.Control
												type="text"
												placeholder="Enter title product"
												value={title}
												onChange={(event) => setTitle(event.target.value)}
											/>
										</Form.Group>
										<Form.Group as={Col} controlId="formState" className="mb-4">
											<Form.Label>Category:</Form.Label>
											<Form.Control
												as="select"
												name="category"
												value={category}
												onChange={(event) => setCategory(event.target.value)}
											>
												<option value="Burger">Burger</option>
												<option value="Pizza">Pizza</option>
												<option value="Sushi">Sushi</option>
											</Form.Control>
										</Form.Group>

										<Form.Group controlId="form.Textarea" className="mb-4">
											<Form.Label>Description:</Form.Label>
											<Form.Control
												as="textarea"
												rows={3}
												value={description}
												onChange={(event) => setDescription(event.target.value)}
											/>
										</Form.Group>

										{img01Preview && (
											<img
												src={img01Preview}
												alt="Preview"
												style={{ width: "25%" }}
											/>
										)}

										<Form.Group controlId="formFile1" className="mb-4">
											<Form.Label>Input general image (image01)</Form.Label>
											<Form.Control
												accept="image/*"
												type="file"
												name="formFile1"
												onChange={(event) =>
													previewPhoto(event, setImg01, setImg01Preview)
												}
											/>
										</Form.Group>

										{img02Preview && (
											<img
												src={img02Preview}
												alt="Preview"
												style={{ width: "25%" }}
											/>
										)}
										<Form.Group controlId="formFile2" className="mb-4">
											<Form.Label>Input image (image02)</Form.Label>
											<Form.Control
												accept="image/*"
												type="file"
												name="formFile2"
												onChange={(event) =>
													previewPhoto(event, setImg02, setImg02Preview)
												}
											/>
										</Form.Group>

										{img03Preview && (
											<img
												src={img03Preview}
												alt="Preview"
												style={{ width: "25%" }}
											/>
										)}
										<Form.Group controlId="formFile3" className="mb-4">
											<Form.Label>Input image (image03)</Form.Label>
											<Form.Control
												accept="image/*"
												type="file"
												name="formFile3"
												onChange={(event) =>
													previewPhoto(event, setImg03, setImg03Preview)
												}
											/>
										</Form.Group>

										<Form.Group className="mb-4">
											<Form.Label>Enter price:</Form.Label>
											<Form.Control
												type="number"
												placeholder="Enter price"
												value={price}
												onChange={(event) => setPrice(event.target.value)}
											/>
										</Form.Group>

										<Button variant="primary" onClick={handleSubmit}>
											Submit
										</Button>
									</Form>
								</Container>
							</div>
						</Tab>
						<Tab eventKey="add-admin" title="Add admin">
							<div className="tab-content">
								<Form>
									<Form.Group controlId="fromBasicFirstName" className="mb-4">
										<div className="d-flex justify-content-between">
											<Form.Label>First Name</Form.Label>
										</div>
										<Form.Control
											onChange={(e) => {
												setFirstName(e.target.value);
											}}
											value={firstName}
											name="firstName"
											type="text"
											placeholder="Enter first name"
										/>
									</Form.Group>

									<Form.Group controlId="fromBasicLastName" className="mb-4">
										<div className="d-flex justify-content-between">
											<Form.Label>Last Name</Form.Label>
										</div>
										<Form.Control
											onChange={(e) => {
												setLastName(e.target.value);
											}}
											value={lastName}
											name="lastName"
											type="text"
											placeholder="Enter last name"
										/>
									</Form.Group>

									<Form.Group controlId="fromBasicEmail" className="mb-4">
										<div className="d-flex justify-content-between">
											<Form.Label>E-mail</Form.Label>
											<AlertText
												paramDirty={emailDirty}
												paramError={emailError}
												paramSuccess="E-mail is good!"
											/>
										</div>
										<Form.Control
											onChange={(e) => emailHandler(e)}
											value={email}
											name="email"
											type="email"
											placeholder="Enter e-mail"
										/>
									</Form.Group>

									<Form.Group controlId="fromBasicPassword" className="mb-4">
										<div className="d-flex justify-content-between">
											<Form.Label>Password</Form.Label>
											<AlertText
												paramDirty={passwordDirty}
												paramError={passwordError}
												paramSuccess="Password is good!"
											/>
										</div>
										<Form.Control
											onChange={(e) => passwordHandler(e)}
											value={password}
											name="password"
											type="password"
											placeholder="Enter password"
										/>
									</Form.Group>

									<Form.Group
										controlId="fromBasicConfirmPassword"
										className="mb-4"
									>
										<div className="d-flex justify-content-between">
											<Form.Label>Confirm the password</Form.Label>
											<AlertText
												paramDirty={confirmPasswordDirty}
												paramError={confirmPasswordError}
												paramSuccess="Confirm password is good!"
											/>
										</div>
										<Form.Control
											onChange={(e) => confirmPasswordHandler(e)}
											value={confirmPassword}
											name="confirmPassword"
											type="password"
											placeholder="Enter confirm password"
										/>
									</Form.Group>
									<Button
										disabled={!formValid}
										className="text-light"
										variant="primary"
										style={{
											backgroundColor: "orangered",
											borderColor: "orangered",
										}}
										onClick={handleRegisterAdminClick}
									>
										Sign Up
									</Button>
								</Form>
							</div>
						</Tab>
						<Tab eventKey="add-courier" title="Add courier">
							<div className="tab-content">
								<Form>
									<Form.Group controlId="fromBasicFirstName" className="mb-4">
										<div className="d-flex justify-content-between">
											<Form.Label>First Name</Form.Label>
										</div>
										<Form.Control
											onChange={(e) => {
												setFirstName(e.target.value);
											}}
											value={firstName}
											name="firstName"
											type="text"
											placeholder="Enter first name"
										/>
									</Form.Group>

									<Form.Group controlId="fromBasicLastName" className="mb-4">
										<div className="d-flex justify-content-between">
											<Form.Label>Last Name</Form.Label>
										</div>
										<Form.Control
											onChange={(e) => {
												setLastName(e.target.value);
											}}
											value={lastName}
											name="lastName"
											type="text"
											placeholder="Enter last name"
										/>
									</Form.Group>

									<Form.Group controlId="fromBasicEmail" className="mb-4">
										<div className="d-flex justify-content-between">
											<Form.Label>E-mail</Form.Label>
											<AlertText
												paramDirty={emailDirty}
												paramError={emailError}
												paramSuccess="E-mail is good!"
											/>
										</div>
										<Form.Control
											onChange={(e) => emailHandler(e)}
											value={email}
											name="email"
											type="email"
											placeholder="Enter e-mail"
										/>
									</Form.Group>

									<Form.Group controlId="fromBasicPassword" className="mb-4">
										<div className="d-flex justify-content-between">
											<Form.Label>Password</Form.Label>
											<AlertText
												paramDirty={passwordDirty}
												paramError={passwordError}
												paramSuccess="Password is good!"
											/>
										</div>
										<Form.Control
											onChange={(e) => passwordHandler(e)}
											value={password}
											name="password"
											type="password"
											placeholder="Enter password"
										/>
									</Form.Group>

									<Form.Group
										controlId="fromBasicConfirmPassword"
										className="mb-4"
									>
										<div className="d-flex justify-content-between">
											<Form.Label>Confirm the password</Form.Label>
											<AlertText
												paramDirty={confirmPasswordDirty}
												paramError={confirmPasswordError}
												paramSuccess="Confirm password is good!"
											/>
										</div>
										<Form.Control
											onChange={(e) => confirmPasswordHandler(e)}
											value={confirmPassword}
											name="confirmPassword"
											type="password"
											placeholder="Enter confirm password"
										/>
									</Form.Group>
									<Button
										disabled={!formValid}
										className="text-light"
										variant="primary"
										style={{
											backgroundColor: "orangered",
											borderColor: "orangered",
										}}
										onClick={handleRegisterCourierClick}
									>
										Sign Up
									</Button>
								</Form>
							</div>
						</Tab>
						<Tab eventKey="order" title="Orders">
							<div className="tab-content">
								{ordersChart.length === 0 ? (
									<h5 className="text-center">No orders</h5>
								) : (
									<div>
										{ordersChart.map((order) => (
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
												<p className="order-info">
													Status: {order.status_order}
												</p>
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
						<Tab eventKey="analitics" title="Analitics">
							<div className="tab-content">
								{/* //////////////////////// */}
								<Tabs
									defaultActiveKey="pop_time_orders"
									id="uncontrolled-tab-example"
									className="mb-3"
								>
									<Tab
										eventKey="pop_time_orders"
										title="The most popular time for orders"
									>
										<div className="tab-content">
											{data.length > 0 ? (
												<PopularOrderTimeChart data={data} />
											) : (
												<p>Loading chart data or no data available...</p>
											)}
										</div>
									</Tab>
									<Tab eventKey="pop_products" title="Popularity of products">
										<div className="tab-content">
											{ordersChart.length > 0 ? (
												<ProductPopularityChart orders={ordersChart} />
											) : (
												<p>Loading chart orders or no data available...</p>
											)}
										</div>
									</Tab>
								</Tabs>
								{/* //////////////////////// */}
							</div>
						</Tab>
					</Tabs>
				) : userInfo.roles.includes("COURIER") ? (
					<Tabs
						defaultActiveKey="order"
						id="uncontrolled-tab-example"
						className="mb-3"
					>
						<Tab eventKey="order" title="Available Orders">
							<div className="tab-content">
								{ordersChart.length === 0 ? (
									<h5 className="text-center">No orders</h5>
								) : (
									<div>
										{ordersChart
											.filter((order) => order.delivery)
											.filter((order) => order.status_order === "PENDING")
											.map((order) => (
												<div key={order.id} className="order-details">
													<input
														type="checkbox"
														checked={selectedOrders.includes(order.id)}
														onChange={() => handleCheckboxChange(order.id)}
													/>
													<label htmlFor={`order-${order.id}`} className="ms-2">
														Select Order ID: {order.id}
													</label>
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
													<p className="order-info">
														Status: {order.status_order}
													</p>
													<h6>Purchase Items:</h6>
													<CartTable
														cartItems={order.purchaseItems}
														showDelete={false}
													/>
												</div>
											))}
										{ordersChart
											.filter((order) => order.delivery)
											.filter((order) => order.status_order === "PENDING")
											.length === 0 ? (
											<></>
										) : (
											<button
												className="btn btn-primary mt-3"
												onClick={handleConfirmSelection}
												disabled={
													ordersChart.filter(
														(order) => order.status_order === "PENDING"
													).length === 0
												}
											>
												Submit
											</button>
										)}
									</div>
								)}
							</div>
						</Tab>
						<Tab
							eventKey="orders for delivery"
							title="Selected orders for delivery"
						>
							{ordersChart
								.filter((order) => order.delivery)
								.map((order) => (
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
										<p className="order-info">Status: {order.status_order}</p>
										<h6>Purchase Items:</h6>
										<CartTable
											cartItems={order.purchaseItems}
											showDelete={false}
										/>
									</div>
								))}
						</Tab>
					</Tabs>
				) : (
					<></>
				)}
			</div>
			{showModal && (
				<ModalAlert
					paramTitle={"General Message"}
					paramBody={showTextModal}
					onShow={showModal}
					onHide={() => setShowModal(false)}
				/>
			)}
		</Helmet>
	);
};

export default Profile;
