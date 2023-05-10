import React, { useState, useEffect, useRef } from "react";


function XYPad({ forwardMessage }) {
    const canvasRef = useRef(null);
    const [position, setPosition] = useState({ x: 0.5, y: 0.5 });
    const [isDragging, setIsDragging] = useState(false);
    const [padDimensions, setPadDimensions] = useState({ width: 0, height: 0, left: 0, top: 0 });


    useEffect(() => {
        const canvas = canvasRef.current;
        const context = canvas.getContext("2d");

        const drawPad = () => {
            context.fillStyle = "#f0f0f0";
            context.fillRect(0, 0, canvas.width, canvas.height);

            const padSize = Math.min(canvas.width, canvas.height) * 0.6;
            const padX = (canvas.width - padSize) / 2;
            const padY = (canvas.height - padSize) / 2;
            const padRadius = padSize / 2;
            setPadDimensions({ width: padSize, height: padSize, left: padX, top: padY });

            context.lineWidth = 4;

            const hue = 360 - Math.floor(position.y * 165);
            const lightness = Math.floor(position.x * 70 + 10);
            const padElementsLightness = 100 - Math.floor(position.x * 100);

            const padColor = `hsl(${hue}, 100%, ${lightness}%)`;
            const padElementsColor = `hsl(0, 0%, ${padElementsLightness}%)`;

            context.fillStyle = padColor;

            context.strokeStyle = padElementsColor

            context.fillRect(padX, padY, padSize, padSize)
            context.strokeRect(padX, padY, padSize, padSize)

            context.beginPath();
            context.moveTo(padX, padY + padRadius);
            context.lineTo(padX + padSize, padY + padRadius);
            context.stroke();

            context.beginPath();
            context.moveTo(padX + padRadius, padY);
            context.lineTo(padX + padRadius, padY + padSize);
            context.stroke();

            const padHandleSize = Math.min(padSize * 0.2, 20);
            setPadDimensions((padDimensions) => ({ ...padDimensions, handleSize: padHandleSize }));
            const handleX = padX + position.x * padSize - padHandleSize / 2;
            const handleY = padY + position.y * padSize - padHandleSize / 2;
            const handleRadius = padHandleSize / 2;

            context.fillStyle = padElementsColor;
            context.beginPath();
            context.arc(handleX + handleRadius, handleY + handleRadius, handleRadius, 0, Math.PI * 2);
            context.fill();

            context.fillStyle = "#000000";
            context.font = "bold 14px Arial";
            context.textAlign = "center";

            context.save();
            context.translate(padX - 20, padY + padSize / 2);
            context.rotate(-Math.PI / 2);
            context.fillStyle = "#000000";
            context.font = "14px sans-serif";
            context.fillText("Dark", 0, 0);
            context.restore();

            context.save();
            context.translate(padX + padSize + 20, padY + padSize / 2);
            context.rotate(Math.PI / 2);
            context.fillStyle = "#000000";
            context.font = "14px sans-serif";
            context.fillText("Light", 0, 0);
            context.restore();

            context.save();
            context.translate(padX + padSize / 2, padY + padSize + 20);
            context.fillStyle = "#000000";
            context.font = "14px sans-serif";
            context.fillText("Calm", 0, 0);
            context.restore();

            context.save();
            context.translate(padX + padSize / 2, padY - 20);
            context.fillStyle = "#000000";
            context.font = "14px sans-serif";
            context.fillText("Intense", 0, 0);
            context.restore();

        };

        drawPad();

    }, [position]);

    const handlePointerDown = (event) => {
        event.preventDefault();
        const clickPosition = { x: event.clientX, y: event.clientY };
        setIsDragging(checkCollision(clickPosition));
    };

    const checkCollision = (clickPosition) => {
        const { left, top, width, height, handleSize } = padDimensions;
        const canvas = canvasRef.current;
        const rect = canvas.getBoundingClientRect();
        const handleX = rect.left + left + position.x * width - handleSize / 2;
        const handleY = rect.top + top + position.y * height - handleSize / 2;

        return (clickPosition.x >= handleX - handleSize && clickPosition.x <= handleX + handleSize &&
            clickPosition.y >= handleY && clickPosition.y - handleSize <= handleY + handleSize)
    }
    const handlePointerMove = (event) => {
        if (isDragging) {
            sendPadMessage(updatePosition(event));
        }
    };
    const handlePointerUp = () => {
        setIsDragging(false);
    };

    const sendPadMessage = (position) => {

        const convertedX = 6.0 - parseFloat((position.x * 4 + 1).toFixed(2));
        const convertedY = 6.0 - parseFloat((position.y * 4 + 1).toFixed(2));

        const message = JSON.stringify({
            event: "PARAMS",
            darkness: convertedX,
            intensity: convertedY,
        });
        forwardMessage(message);
    };


    const updatePosition = (event) => {
        const canvas = canvasRef.current;
        const rect = canvas.getBoundingClientRect();
        let x = (event.clientX - rect.left - padDimensions.left) / padDimensions.width;
        let y = (event.clientY - rect.top - padDimensions.top) / padDimensions.height;

        x = Math.min(1, Math.max(0, x));
        y = Math.min(1, Math.max(0, y));

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
        </div>
    );
}

export default XYPad;



