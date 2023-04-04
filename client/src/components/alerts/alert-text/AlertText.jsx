import React from "react";
import { Form } from "react-bootstrap";

const AlertText = ({ paramDirty, paramError, paramSuccess }) => {
	return (
		paramDirty && (
			<>
				{paramError ? (
					<Form.Text className="text-danger">❌ {paramError}</Form.Text>
				) : (
					<Form.Text className="text-success">✔️ {paramSuccess}</Form.Text>
				)}
			</>
		)
	);
};

export default AlertText;
