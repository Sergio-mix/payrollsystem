/**
 * @description: waiting page
 */
const ChargingPad = (props) => {
    return (
        <div className={"div-center"}>
            <div className={"form-main p-5 border-radius-main box-shadow-main text-center d-block p-2 effect-main"}>
                <h1 className={"text-color-aux"}>{props.title}</h1>
                <span className={"font-size-20 text-color-grey"}>{props.description}</span>
                <br/>
                <div className="spinner">
                    <div className="cube1"></div>
                    <div className="cube2"></div>
                </div>
                {props.content}
            </div>
        </div>
    );
}

export default ChargingPad;