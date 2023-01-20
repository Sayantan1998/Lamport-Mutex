import os

def read_File(rootDir,pathSet):
    for lists in os.listdir(rootDir):
        path = os.path.join(rootDir, lists)
        pathSet.append(path)
        if os.path.isdir(path):
            read_File(path,pathSet)


def main():
    file_Directory = []
    Time_Stamps = []
    read_File(os.getcwd(),file_Directory)
    for dir in file_Dirrectory:
	if	(dir[-12:-6] == "config") and (dir[-3:] == "out"):
            f = open(dir)
            lines = f.readlines()
            for index in range(len(lines)/2):
                Time_Stamps.append(int(lines[index*2].split(' ')[1]))
    
    test = list(set(Time_Stamps))
    if (len(Time_Stamps) == len(test)):
       print "Hoorah!\nTesting Passed !\nBecause Time to Enter into the Critical Section are Different"
    else:
		print "Testing  Failed"
                

if __name__ == '__main__':
    main()
