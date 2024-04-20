# GyroGraphs App - Mobile Computing Submission

This application is designed to analyze sensor data collected from a mobile device and provide predictions using the ARIMA (AutoRegressive Integrated Moving Average) model. It includes components for data preprocessing, model training, and visualization of predictions.

## Components:

1. **Data Preprocessing**: 
   - The application reads sensor data from a text file using the Pandas library.
   - It assumes the sensor data file is in the following format: each row contains measurements for the x, y, z axes, and a timestamp in milliseconds.
   - Timestamps are converted to datetime format using Pandas `to_datetime` function.
   - The data is then split into training and testing sets. By default, 80% of the data is used for training, and 20% for testing.

2. **ARIMA Model Training**:
   - For each axis (x, y, z) of the sensor data, an ARIMA model is trained.
   - The ARIMA parameters (p, d, q) are determined empirically or by using techniques like grid search or information criteria (e.g., AIC, BIC).
   - The ARIMA model is fitted to the training data using the `ARIMA` class from the `statsmodels.tsa.arima.model` module.
   - Once trained, the model is used to generate predictions for the testing set.

3. **Visualization**:
   - Matplotlib library is used for visualizing the sensor data and predictions.
   - For each axis (x, y, z), a plot is generated showing the actual sensor data and the predicted values.
   - Additionally, a combined plot is created to display predictions for all three axes.

## Usage:

1. **Data Collection**:
   - Collect sensor data on your mobile device using an appropriate app or tool.
   - Transfer the sensor data file (e.g., `sensor_data.txt`) to your desktop computer.

2. **Running the Application**:
   - Ensure you have Python installed on your system.
   - Install the required dependencies listed in the `requirements.txt` file using `pip install -r requirements.txt`.
   - Run the `arima.py` script using Python.
   - The application will process the sensor data, train ARIMA models, generate predictions, and save the plots in the same directory.

3. **Interpreting Results**:
   - Once the application completes execution, you will find the plots saved as `x.jpg`, `y.jpg`, `z.jpg`, and `all_axes_predictions.jpg` in the same directory.
   - Each plot compares the actual sensor data with the predicted values for the corresponding axis.
   - The combined plot (`all_axes_predictions.jpg`) shows predictions for all three axes.

4. **Adjustments**:
   - You can modify the ARIMA parameters (p, d, q) in the `orders` list to experiment with different model configurations.
   - Additionally, you can customize the visualization settings in the code to suit your preferences.

## Notes:
- The application assumes that the sensor data file is in the specified format and contains measurements for the x, y, z axes along with timestamps.
- It is recommended to transfer the sensor data file from the mobile device to the desktop for processing.
- The code utilizes Python libraries such as Pandas, statsmodels, and Matplotlib for data manipulation, model training, and visualization.
