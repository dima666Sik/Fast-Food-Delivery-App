import React, { useEffect, useState } from "react";
import { Form, Modal, Button } from "react-bootstrap";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";

import { useValidationAuthForms } from "../hooks/useValidationAuthForms";
import AlertText from "../components/alerts/AlertText";
import { useValidFormsBtn } from "../hooks/useValidFormsBtn";
import { clearUser, setInfoUser, setUser } from "../redux/store/user/userSlice";
import { cartActionsLiked } from "../redux/store/shopping-cart/cartsLikedSlice.js";
import { cartActions } from "../redux/store/shopping-cart/cartSlice";
import ModalAlert from "../components/alerts/ModalAlert";

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
	const [showTextModal, setShowTextModal] = useState("");
	const [showModal, setShowModal] = useState(false);
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
			console.log("hi");
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
			console.log(error);
			// console.error("Failed to log in");
			setShowTextModal(error.response.data.message_response);
			setShowModal(true);
		}
	};

	useEffect(() => {
		if (emailError || passwordError) setFormValid(false);
		else setFormValid(true);
	}, [emailError, passwordError]);

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
			{showModal && (
				<ModalAlert
					paramTitle={"Error Authenticated"}
					paramBody={showTextModal}
					onShow={showModal}
					onHide={() => setShowModal(false)}
				/>
			)}
		</>
	);
};

export default Login;
