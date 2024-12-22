import axios from "axios";

export const deleteReview = async (
	accessToken,
	product_id,
	product_review_id
) => {
	try {
		console.log(accessToken, product_id, product_review_id);
		const response = axios.delete(
			`${process.env.REACT_APP_SERVER_API_URL}api/v2/product/private/food-reviews/delete-review-to-product?product_id=${product_id}&product_review_id=${product_review_id}`,
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
		return response;
	} catch (error) {
		console.log(error.response.data.message);
		throw error;
	}
};
