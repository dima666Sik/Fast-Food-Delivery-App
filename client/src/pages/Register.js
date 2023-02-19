import React, { useEffect, useState } from "react";
import { Form, Modal, Button } from "react-bootstrap";

const Register = (props) => {
	const handleLoginClick = () => {
		setEmailDirty(false);
		setPasswordDirty(false);
		setConfirmPasswordDirty(false);
		setEmail("");
		setPassword("");
		setConfirmPassword("");
		props.onHide();
		props.onLoginClick();
	};

	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [confirmPassword, setConfirmPassword] = useState("");

	const [emailDirty, setEmailDirty] = useState(false);
	const [passwordDirty, setPasswordDirty] = useState(false);
	const [confirmPasswordDirty, setConfirmPasswordDirty] = useState(false);

	const [emailError, setEmailError] = useState("E-mail field cannot be empty!");
	const [passwordError, setPasswordError] = useState(
		"Password field cannot be empty!"
	);
	const [confirmPasswordError, setConfirmPasswordError] = useState(
		"Confirm password field cannot be empty!"
	);

	const [formValid, setFormValid] = useState(false);

	useEffect(() => {
		if (emailError || passwordError) setFormValid(false);
		else setFormValid(true);
	}, [emailError, passwordError]);

	const emailHandler = (e) => {
		if (e.target.value !== 0) {
			const rgx =
				/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			const currentEmail = e.target.value;
			setEmail(currentEmail);
			if (!rgx.test(String(currentEmail).toLowerCase())) {
				setEmailError("Incorrect E-mail");
				if (!e.target.value.length)
					setEmailError("E-mail field cannot be empty!");
			} else {
				setEmailError("");
			}
		}
	};

	const passwordHandler = (e) => {
		if (e.target.value !== 0) {
			const currentPass = e.target.value;
			setPassword(currentPass);
			if (currentPass.length < 6 || currentPass.length > 8) {
				setPasswordError("Password less 6 or large 8 chars!");
				if (!e.target.value.length)
					setPasswordError("Password field cannot be empty!");
			} else {
				setPasswordError("");
			}
		}
	};

	const confirmPasswordHandler = (e) => {
		if (e.target.value !== 0) {
			const currentConfirmPass = e.target.value;
			setConfirmPassword(currentConfirmPass);
			console.log("ccp:" + currentConfirmPass + " p: " + password);
			if (currentConfirmPass !== password) {
				setConfirmPasswordError("Confirm password and password isn`t equals!");
				if (!e.target.value.length)
					setConfirmPasswordError("Confirm password field cannot be empty!");
			} else {
				setConfirmPasswordError("");
			}
		}
	};

	const blurHandler = (e) => {
		switch (e.target.name) {
			case "email": {
				setEmailDirty(true);
				break;
			}
			case "password":
				setPasswordDirty(true);
				break;
			case "confirmPassword":
				setConfirmPasswordDirty(true);
				break;
			default:
				break;
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
							<Form.Control type="text" placeholder="Enter first name" />
						</Form.Group>

						<Form.Group controlId="fromBasicLastName">
							<div className="d-flex justify-content-between">
								<Form.Label>Last Name</Form.Label>
							</div>
							<Form.Control type="text" placeholder="Enter last name" />
						</Form.Group>

						<Form.Group controlId="fromBasicEmail">
							<div className="d-flex justify-content-between">
								<Form.Label>E-mail</Form.Label>
								{emailDirty && emailError && (
									<Form.Text className="text-danger">{emailError}</Form.Text>
								)}
							</div>
							<Form.Control
								onChange={(e) => emailHandler(e)}
								value={email}
								onBlur={(e) => blurHandler(e)}
								name="email"
								type="email"
								placeholder="Enter e-mail"
							/>
						</Form.Group>

						<Form.Group controlId="fromBasicPassword">
							<div className="d-flex justify-content-between">
								<Form.Label>Password</Form.Label>
								{passwordDirty && passwordError && (
									<Form.Text className="text-danger">{passwordError}</Form.Text>
								)}
							</div>
							<Form.Control
								onChange={(e) => passwordHandler(e)}
								value={password}
								onBlur={(e) => blurHandler(e)}
								name="password"
								type="password"
								placeholder="Enter password"
							/>
						</Form.Group>

						<Form.Group controlId="fromBasicConfirmPassword">
							<div className="d-flex justify-content-between">
								<Form.Label>Confirm the password</Form.Label>
								{confirmPasswordDirty && confirmPasswordError && (
									<Form.Text className="text-danger">
										{confirmPasswordError}
									</Form.Text>
								)}
							</div>
							<Form.Control
								onChange={(e) => confirmPasswordHandler(e)}
								value={confirmPassword}
								onBlur={(e) => blurHandler(e)}
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
		</>
	);
};

export default Register;
