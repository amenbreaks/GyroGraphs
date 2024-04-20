import pandas as pd
from statsmodels.tsa.arima.model import ARIMA
import matplotlib
import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator

file_path = "sensor_data.txt"
sensor_data = pd.read_csv(file_path, header=None, names=["x", "y", "z", "timestamp"])

sensor_data['timestamp'] = pd.to_datetime(sensor_data['timestamp'], unit='ms')
sensor_data.set_index('timestamp', inplace=True)
sensor_data.index = pd.DatetimeIndex(sensor_data.index).to_period('ms')

train_size = int(len(sensor_data) * 0.8)
train_data, test_data = sensor_data[:train_size], sensor_data[train_size:]

orders = [(5, 1, 0), (5, 1, 0), (5, 1, 0)]  # ARIMA parameters: (p, d, q)

predictions = []
plt.figure(figsize=(12, 18))
for i, axis in enumerate(['x', 'y', 'z']):
    model = ARIMA(train_data[axis], order=orders[i])
    model_fit = model.fit()
    forecast = model_fit.forecast(steps=len(test_data))
    predictions.append(forecast)

    ax = plt.subplot(3, 1, i + 1)
    ax.plot(test_data.index.astype(str), test_data[axis], label='Actual')
    ax.plot(test_data.index.astype(str), predictions[i], color='red', label='Predicted')
    ax.set_title(f'ARIMA Prediction - {axis.upper()} Axis: Actual vs. Predicted')
    ax.set_xlabel('Time (Seconds)')
    ax.set_ylabel(f'{axis.upper()}-axis Value')
    ax.legend()
    ax.xaxis.set_major_locator(MaxNLocator(integer=True))  # Show only integer ticks on the x-axis
    plt.xticks(rotation=45)  # Rotate x-axis labels for better readability

plt.tight_layout()  # Adjust layout to prevent overlapping
plt.savefig('all_axes_predictions.jpg', bbox_inches='tight')  # Save plot with tight bounding box
print("Plots saved as all_axes_predictions.jpg")
plt.close()
