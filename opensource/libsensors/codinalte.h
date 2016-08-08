/*
 * Copyright (C) 2008 The Android Open Source Project
 * Copyright (C) 2016 Jonathan Jason Dennis [Meticulus]
 *									theonejohnnyd@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* Sensor handles */
#define HANDLE_ACCELEROMETER	(0)
#define HANDLE_MAGNETIC_FIELD	(1)
#define HANDLE_ORIENTATION		(2)
#define HANDLE_GYROSCOPE		(3)
#define HANDLE_LIGHT			(4)
#define HANDLE_PRESSURE			(5)
#define HANDLE_TEMPERATURE		(6)
#define HANDLE_PROXIMITY		(8)
#define HANDLE_MAX			    (9)

/* Sensor handles */
#define MINDELAY_ACCELEROMETER	(1000)
#define MINDELAY_MAGNETIC_FIELD	(1000)
#define MINDELAY_ORIENTATION	(1000)
#define MINDELAY_GYROSCOPE		(1000)
#define MINDELAY_LIGHT			(0)
#define MINDELAY_PRESSURE		(1000)
#define MINDELAY_TEMPERATURE	(0)
#define MINDELAY_PROXIMITY		(0)

/* Constants */
#define LSM_M_MAX_CAL_COUNT 300
#define RADIANS_TO_DEGREES (180.0/M_PI)
#define DEGREES_TO_RADIANS (M_PI/180.0)
#define MAX_LENGTH 150
#define SIZE_OF_BUF 100

/* Functions */
#define CONVERT_A  (GRAVITY_EARTH * (1.0f/1000.0f))

/* Alps defines (kyle) */
#define ALPSIO	0xAF
#define ALPSIO_SET_MAGACTIVATE   _IOW(ALPSIO, 0, int)
#define ALPSIO_SET_ACCACTIVATE   _IOW(ALPSIO, 1, int)
#define ALPSIO_SET_DELAY         _IOW(ALPSIO, 2, int)

/* Magnetometer defines */
#define HSCDTD008A_POWER 0.2f
#define HSCDTD008A_RANGE 4800.0f
#define HSCDTD008A_RESOLUTION 0.14992504f

/* Accelerometer defines */
#define BMA254_POWER  0.2f
#define BMA254_RANGE  39.24f
#define BMA254_RESOLUTION  0.009580079f

/* proximity defines */
#define TAOS_POWER 0.75f
#define TAOS_RANGE  5.0f
#define TAOS_RESOLUTION  5.0f

/* Orientation defines */
#define ALPS_POWER 0.2f
#define ALPS_RANGE 360.0f
#define ALPS_RESOLUTION 1.0f

/* magnetometer paths*/
char const *const PATH_DATA_MAG =
		"/sys/class/sensors/magnetic_sensor/adc";

/* accelerometer paths*/
char const *const PATH_DATA_ACC =
		"/sys/class/sensors/accelerometer_sensor/raw_data";

/* proximity paths*/
char const *const PATH_POWER_PROX =
		"/sys/class/input/input5/enable";
char const *const PATH_DATA_PROX =
		"/sys/class/sensors/proximity_sensor/adc";
char const *const PATH_INTR_PROX =
                  "/dev/input/event5";

/* alps (kyle) paths */

char const *const PATH_IO_ALPS =
		"/dev/alps_io";


/* Accelerometer sensor path structure */
typedef struct {
    char path_mode[MAX_LENGTH];
    char path_range[MAX_LENGTH];
    char path_rate[MAX_LENGTH];
    char path_data[MAX_LENGTH];
    char gyro_path_mode[MAX_LENGTH];
    char gyro_path_rate[MAX_LENGTH];
    char gyro_path_data[MAX_LENGTH];
    char gyro_path_sensitivity[MAX_LENGTH];
    char magn_range[MAX_LENGTH];
} Sensor_data;

/* To store all Sensors data*/
typedef struct {
   sensors_event_t sensor_data[8];
   int length;
} Sensor_messagequeue;

typedef struct {
    int prox_val;
    char prox_flag;
}Sensor_prox;

/* sensor API integration */

static const struct sensor_t sSensorList[] = {
	{"TAOS Proximity Sensor",
		"Meticulus/TAOS",
		1,
		HANDLE_PROXIMITY,
		SENSOR_TYPE_PROXIMITY,
		TAOS_RANGE,
		TAOS_RESOLUTION,
		TAOS_POWER,
		MINDELAY_PROXIMITY,
		0,
		0,
		"vu.co.meticulus.taos.proximity",
		"",
		200000,
		0,
		NULL,
	},
	{"HSCDTD008A Magnetic Sensor",
		"alps electric co., ltd.",
		1,
		HANDLE_MAGNETIC_FIELD,
		SENSOR_TYPE_MAGNETIC_FIELD,
		HSCDTD008A_RANGE,
		HSCDTD008A_RESOLUTION,
		HSCDTD008A_POWER,
		MINDELAY_MAGNETIC_FIELD,
		0,
		0,
		"vu.co.meticulus.alps.magnetic",
		"",
		200000,
		0,
		NULL,
	},
	{"BMA254 Accelerometer",
		"Bosch Corporation",
		1,
		HANDLE_ACCELEROMETER,
		SENSOR_TYPE_ACCELEROMETER,
		BMA254_RANGE,
		BMA254_RESOLUTION,
		BMA254_POWER,
		MINDELAY_ACCELEROMETER,
		0,
		0,
		"vu.co.meticulus.bosch.accerometer",
		"",
		200000,
		0,
		NULL,
	},
	{"ALPS Orientation Sensor",
		"alps electric co., ltd",
		1,
		HANDLE_ORIENTATION,
		SENSOR_TYPE_ORIENTATION,
		ALPS_RANGE,
		ALPS_RESOLUTION,
		ALPS_POWER,
		MINDELAY_ORIENTATION,
		0,
		0,
		"vu.co.meticulus.alps.orientation",
		"",
		200000,
		0,
		NULL,
	},
};

static int acc_id;
