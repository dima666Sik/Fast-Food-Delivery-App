import React, { useState, useEffect } from "react";
import { Form, Modal, Button } from "react-bootstrap";

const Login = (props) => {
	const handleRegisterClick = () => {
		setEmailDirty(false);
		setPasswordDirty(false);
		setEmail("");
		setPassword("");
		props.onHide();
		props.onRegisterClick();
	};

	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");

	const [emailDirty, setEmailDirty] = useState(false);
	const [passwordDirty, setPasswordDirty] = useState(false);

	const [emailError, setEmailError] = useState("E-mail field cannot be empty!");
	const [passwordError, setPasswordError] = useState(
		"Password field cannot be empty!"
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
				if (!e.target.value.length) {
					setEmailError("E-mail field cannot be empty!");
					setTimeout(() => setEmailError(""), 2000);
				}
			} else {
				setEmailError("");
			}
		}
	};

	const passwordHandler = (e) => {
		if (e.target.value !== 0) {
			const currentPass = e.target.value;
			setPassword(currentPass);
			if (!e.target.value.length) {
				setPasswordError("Password field cannot be empty!");
				setTimeout(() => setPasswordError(""), 2000);
			} else setPasswordError("");
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
					<Modal.Title>Log In</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<Form>
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
							<Form.Text className="text-muted">
								We will never share your e-mail with anyone else.
							</Form.Text>
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
