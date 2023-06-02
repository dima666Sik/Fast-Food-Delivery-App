import axios from "axios";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";

export const useGetAllProducts = () => {
	const [products, setProducts] = useState([]);
	const [isLoading, setIsLoading] = useState(false);

	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);
	const accessToken = useSelector((state) => state.user.accessToken);
	useEffect(() => {
		setIsLoading(true);
		const fetchData = async () => {
			axios
				.get(
					`${process.env.REACT_APP_SERVER_API_URL}api/v1/foods/get-all-products`
				)
				.then((response) => {
					// setTimeout(() => {
					const updatedProducts = response.data.map((product) => {
						return {
							...product,
							image01: `${process.env.REACT_APP_SERVER_API_URL}public/images/products/${product.image01}`,
							image02: `${process.env.REACT_APP_SERVER_API_URL}public/images/products/${product.image02}`,
							image03: `${process.env.REACT_APP_SERVER_API_URL}public/images/products/${product.image03}`,
						};
					});
					setProducts(updatedProducts);
					setIsLoading(false);
					// }, 4000);
				})
				.catch((error) => {
					if (error.code === "ERR_NETWORK") {
						console.log("Error network, please try again...");
					}
					console.log(error);
				});
		};
		fetchData();
	}, [isAuthenticated]);

	return { products, setProducts, isLoading, setIsLoading };
};
