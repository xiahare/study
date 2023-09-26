#!/bin/bash

SQL_FILE_NAME=$1

impala-shell -d db_log_public -f "${SQL_FILE_NAME}"