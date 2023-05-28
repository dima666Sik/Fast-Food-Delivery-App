import { useState } from "react";

export const useReview = () => {
	const [review, setReview] = useState("");

	const [reviewDirty, setReviewDirty] = useState(false);

	const [reviewError, setReviewError] = useState(
		"Review field cannot be empty!"
	);

	const reviewHandler = (e) => {
		if (e.target.value !== 0) {
			const currentReview = e.target.value;
			setReview(currentReview);
			if (!e.target.value.length) {
				setReviewError("Review field cannot be empty!");
			} else {
				setReviewError("");
			}
		}
		setReviewDirty(true);
	};

	return {
		review,
		setReview,
		reviewDirty,
		setReviewDirty,
		reviewError,
		setReviewError,
		reviewHandler,
	};
};
