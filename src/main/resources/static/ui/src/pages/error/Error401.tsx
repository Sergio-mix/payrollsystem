import ErrorPage from "../../components/util/ErrorPage";

const Error401 = () => {
    return (
        <ErrorPage code={[4, 0, 1]} title={"No access"} message={"You do not have access to this resource"}/>
    )
}
export default Error401;