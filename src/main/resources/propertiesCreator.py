import csv
import shutil

filename = "./src/main/resources/uebersetzungen.csv"

fields = []
rows = []

#read and save csv file in lists
with open(filename, 'r') as csvfile:
    csvreader = csv.reader(csvfile)
    fields = next(csvreader)[0].split(';')

    for row in csvreader:
        rows.append(row[0].split(';'))

#write and save property files
for i in range(1, len(fields)):
    filename = "./src/main/resources/messages_" + fields[i] + ".properties"

    with open(filename, 'w') as file:
        for row in rows:
            print(row[0] + '=' + row[i], file=file)

"""
with open('ueb04-uebersetzungen.csv') as csvfile:
    de = open("message_de.properties", "w")
    en = open("message_en.properties", "w")
    nl = open("message_nl.properties", "w")
    csvreader = csv.reader(csvfile, delimiter=',')
    for row in csvfile:
        lines = row.split(';')
        de.write(f'{lines[0]}={lines[1]}\n')
        en.write(f'{lines[0]}={lines[2]}\n')
        nl.write(f'{lines[0]}={lines[3]}\n')
"""

#create fallback property file
shutil.copy("./src/main/resources/messages_en.properties", "./src/main/resources/messages.properties")
