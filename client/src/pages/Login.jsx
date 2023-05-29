import React, { useEffect } from "react";
import { Form, Modal, Button } from "react-bootstrap";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";

import { useValidationAuthForms } from "../hooks/useValidationAuthForms";
import AlertText from "../components/alerts/alert-text/AlertText";
import { useValidFormsBtn } from "../hooks/useValidFormsBtn";
import { clearUser, setInfoUser, setUser } from "../redux/store/user/userSlice";
import { cartActionsLiked } from "../redux/store/shopping-cart/cartsLikedSlice.js";
import { cartActions } from "../redux/store/shopping-cart/cartSlice";

const Login = (props) => {
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
	} = useValidationAuthForms();

	const { formValid, setFormValid } = useValidFormsBtn();

	const handleRegisterClick = () => {
		setEmailDirty(false);
		setPasswordDirty(false);
		setEmail("");
		setPassword("");
		props.onHide();
		props.onRegisterClick();
		setFormValid(false);
	};

	const dispatch = useDispatch();

	const accessToken = useSelector((state) => state.user.accessToken);

	const handleSignInClick = async () => {
		try {
			const response = await axios.post(
				`${process.env.REACT_APP_SERVER_API_URL}api/v1/auth/login`,
				{ email, password },
				{ headers: { "Content-Type": "application/json" } }
			);
			console.log("Logged in:", response.data);

			if (response.data.message_response.status_response) {
				dispatch(setUser({ accessToken: response.data.access_token }));
				dispatch(cartActions.clearCart());
				props.onHide();
			}
		} catch (error) {
			console.error("Failed to log in");
			// вы можете установить здесь состояние ошибки и отображать его с помощью AlertText
		}
	};

	useEffect(() => {
		if (emailError || passwordError) setFormValid(false);
		else setFormValid(true);
	}, [emailError, passwordError]);

	useEffect(() => {
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
				.catch((error) => {
					console.log(error.response);
					if (error.response.data.access_token) {
						// Dispatch the setUser action
						dispatch(
							setUser({
								accessToken: error.response.data.access_token,
							})
						);
					}

					if (
						error.response.data ===
							"You need to reauthorize! Tokens all were expired. You will be much to authorization!" ||
						error.response.data === "Valid Refresh token was expired..."
					) {
						dispatch(clearUser());
						dispatch(cartActions.clearCart());
						dispatch(cartActionsLiked.clearCartsLiked());
					}
				});
		}
	}, [accessToken]);

	return (
		<>
			<Modal
				aria-labelledby="contained-modal-title-vcenter"
				centered
				show={props.show}
				onHide={props.onHide}
			>
				<Modal.Header closeButton>
					<Modal.Title>Log In</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<Form>
						<Form.Group controlId="fromBasicEmail">
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
							<Form.Text className="text-muted">
								We will never share your e-mail with anyone else.
							</Form.Text>
						</Form.Group>

						<Form.Group controlId="fromBasicPassword">
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
							<Form.Text className="text-muted">
								We will never share your password with anyone else.
							</Form.Text>
						</Form.Group>

						<Modal.Footer>
							<Button variant="secondary" onClick={handleRegisterClick}>
								Sign Up
							</Button>

							<Button
								disabled={!formValid}
								className="text-light"
								variant="primary"
								style={{
									backgroundColor: "orangered",
									borderColor: "orangered",
								}}
								onClick={handleSignInClick}
							>
								Sign In
							</Button>
						</Modal.Footer>
						<Form.Text>
							If you do not have an account, click on 'Sign Up' to create one.
						</Form.Text>
					</Form>
				</Modal.Body>
			</Modal>
		</>
	);
};

export default Login;
