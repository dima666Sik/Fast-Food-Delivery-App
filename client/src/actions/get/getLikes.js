import axios from "axios";

// export const getLike = async (idProduct, accessToken) => {
// 	await axios
// 		.get(
// 			`${process.env.REACT_APP_SERVER_API_URL}api/v1/private/get-like-product?product_id=${idProduct}`,
// 			{
// 				headers: {
// 					Authorization: "Bearer " + accessToken,
// 				},
// 			}
// 		)
// 		.then((res) => {
// 			return res.data;
// 		})
// 		.catch((error) => {
// 			console.error(error);
// 		});
// };

// export const getStatus = async (idProduct, accessToken) => {
// 	await axios
// 		.get(
// 			`${process.env.REACT_APP_SERVER_API_URL}api/v1/private/get-status-product?product_id=${idProduct}`,
// 			{
// 				headers: {
// 					Authorization: "Bearer " + accessToken,
// 				},
// 			}
// 		)
// 		.then((res) => {
// 			console.log(res.data);
// 		})
// 		.catch((error) => {
// 			console.error(error);
// 		});
// };

export const getListStatusForUser = async (accessToken) => {
	try {
		const response = await axios.get(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/private/product-like/get-status-products`,
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
		console.log(response.data);
		return response.data;
	} catch (error) {
		console.error(error);
		throw error;
	}
};
