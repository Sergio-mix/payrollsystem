import {useEffect, useState} from "react";
import { useNavigate } from "react-router-dom";

/**
 * @description: Error type page
 * @param props Error code
 */
const ErrorPage = (props) => {
    const [error, setError] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        let list = [];
        for (let i = 0; i < 16; i++) {
            list.push(
                [
                    <span className="particle">{props.code[0]}</span>,
                    <span className="particle">{props.code[1]}</span>,
                    <span className="particle">{props.code[2]}</span>
                ]
            );
        }
        setError(list);// set error
    }, []);

    return (
        <div className="container-error">
            {error}
            <article className="content box-shadow-main">
                <h1>{props.title}</h1>
                <p>{props.message}</p>
                <p>
                    <button onClick={() => navigate("/")}>Login</button>
                </p>
            </article>
        </div>
    );
}

export default ErrorPage;