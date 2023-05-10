import React, { useState, useEffect, useRef } from "react";


function XY3({ sendMessage }) {
    const canvasRef = useRef(null);
    const [position, setPosition] = useState({ x: 0.5, y: 0.5 });
    const [isDragging, setIsDragging] = useState(false);

    useEffect(() => {
        const canvas = canvasRef.current;
        const context = canvas.getContext("2d");

        const drawPad = () => {
            context.fillStyle = "#f0f0f0";
            context.fillRect(0, 0, canvas.width, canvas.height);

            const padSize = Math.min(canvas.width, canvas.height) * 0.8;
            const padX = (canvas.width - padSize) / 2;
            const padY = (canvas.height - padSize) / 2;
            const padRadius = padSize / 2;

            context.strokeStyle = "#000000";
            context.lineWidth = 4;
            context.beginPath();
            context.arc(padX + padRadius, padY + padRadius, padRadius, 0, Math.PI * 2);
            context.stroke();

            context.beginPath();
            context.moveTo(padX, padY + padRadius);
            context.lineTo(padX + padSize, padY + padRadius);
            context.stroke();

            context.beginPath();
            context.moveTo(padX + padRadius, padY);
            context.lineTo(padX + padRadius, padY + padSize);
            context.stroke();

            const padHandleSize = Math.min(padSize * 0.2, 20);
            const handleX = padX + position.x * padSize - padHandleSize / 2;
            const handleY = padY + position.y * padSize - padHandleSize / 2;

            context.fillStyle = "#000000";
            context.fillRect(handleX, handleY, padHandleSize, padHandleSize);
        };

        drawPad();

    }, [position]);

    const handlePointerDown = (event) => {
        event.preventDefault();
        setIsDragging(true);
        //updatePosition(event);
    };

    const handlePointerMove = (event) => {
        if (isDragging) {
            console.log("handlePointerMove")
            sendPadMessage(updatePosition(event));
        }
    };
    const handlePointerUp = () => {
        setIsDragging(false);
    };

    const sendPadMessage = (position) => {

        const convertedX = parseFloat((position.x * 4 + 1).toFixed(2));
        const convertedY = parseFloat((position.y * 4 + 1).toFixed(2));


        const message = JSON.stringify({
            event: "PARAMS",
            darkness: convertedX,
            intensity: convertedY,
        });

        sendMessage(message);
    };


    const updatePosition = (event) => {
        const canvas = canvasRef.current;
        const rect = canvas.getBoundingClientRect();
        const x = (event.clientX - rect.left) / canvas.width;
        const y = (event.clientY - rect.top) / canvas.height;
        const newPosition = { x: x, y: y };
        setPosition(newPosition);
        return newPosition;
    };

    return (
        <div className="xy-pad">
            <canvas
                ref={canvasRef}
                width={400}
                height={400}
                onPointerDown={handlePointerDown}
                onPointerMove={handlePointerMove}
                onPointerUp={handlePointerUp}
            />
            {/* <button onClick={() => sendMessage("PARAMS", 3, 4)}>Send Message</button> */}
        </div>
    );
}

export default XY3;



