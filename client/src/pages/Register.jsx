import React, { useEffect, useState } from "react";
import { Form, Modal, Button } from "react-bootstrap";

import { useValidationAuthForms } from "../hooks/useValidationAuthForms";
import AlertText from "../components/alerts/AlertText";
import { useValidFormsBtn } from "../hooks/useValidFormsBtn";
import ModalAlert from "../components/alerts/ModalAlert";
import { register } from "../actions/post/register";

const Register = (props) => {
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
	const [showTextModal, setShowTextModal] = useState("");
	const [showModal, setShowModal] = useState(false);

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

	const handleLoginClick = () => {
		setEmailDirty(false);
		setPasswordDirty(false);
		setConfirmPasswordDirty(false);
		setFirstName("");
		setLastName("");
		setEmail("");
		setPassword("");
		setConfirmPassword("");
		props.onHide();
		props.onLoginClick();
		setFormValid(false);
	};

	const handleRegisterClick = async () => {
		const userData = {
			first_name: firstName,
			last_name: lastName,
			email: email,
			password: password,
			is_admin: false,
		};

		try {
			const response = await register(userData);

			if (response.status === 200) {
				console.log(userData);
				console.log("Register in:", response.data);
				handleLoginClick();
			}
		} catch (error) {
			console.error(error);
			setShowTextModal(error.response.data.message_response);
			setShowModal(true);
		}
	};

	return (
		<>
			<Modal
				aria-labelledby="contained-modal-title-vcenter"
				centered
				show={props.show}
				onHide={props.onHide}
			>
				<Modal.Header closeButton>
					<Modal.Title>Sign Up</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<Form>
						<Form.Group controlId="fromBasicFirstName">
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

						<Form.Group controlId="fromBasicLastName">
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
						</Form.Group>

						<Form.Group controlId="fromBasicConfirmPassword">
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

						<Modal.Footer>
							<Button variant="secondary" onClick={handleLoginClick}>
								Sign In
							</Button>
							<Button
								disabled={!formValid}
								className="text-light"
								variant="primary"
								style={{
									backgroundColor: "orangered",
									borderColor: "orangered",
								}}
								onClick={handleRegisterClick}
							>
								Sign Up
							</Button>
						</Modal.Footer>
						<Form.Text>
							If you have an account, click on 'Sign In' to enter one.
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

export default Register;
